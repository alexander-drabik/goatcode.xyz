package xyz.goatcode.chess.controllers;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.util.UriBuilder;
import xyz.goatcode.chess.logic.Logic;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.URIParameter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ChessController {
    HashMap<String, Short[]> gameMap = new HashMap<>();
    HashMap<String, Boolean> move = new HashMap<>();
    HashMap<Pair<String, String>, Boolean> color = new HashMap<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @RequestMapping("/chess{code}")
    public String chess(@PathVariable("code") String code) {
        return "board";
    }

    @PostMapping(value = "/chess/board/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getBoard(@RequestBody String code, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!gameMap.containsKey(code)) {
            gameMap.put(code, new Short[] {
                    5 ,  4,  3,  2,  1,  3,  4,  5,
                    6 ,  6,  6,  6,  6,  6,  6,  6,
                    0 ,  0,  0,  0,  0,  0,  0,  0,
                    0 ,  0,  0,  0,  0,  0,  0,  0,
                    0 ,  0,  0,  0,  0,  0,  0,  0,
                    0 ,  0,  0,  0,  0,  0,  0,  0,
                    -6, -6, -6, -6, -6, -6, -6, -6,
                    -5, -4, -3, -2, -1, -3, -4, -5,
            });
            move.put(code, true);
            color.put(new Pair<>(code, ip), true);
            System.out.println("1. " + ip + color.get(new Pair<>(code, ip)));
        } else {
            if (!color.containsKey(new Pair<>(code, ip))) {
                color.put(new Pair<>(code, ip), false);
                System.out.println("2. " +ip + color.get(new Pair<>(code, ip)));
            }
        }
        System.out.println(ip);

        Short[] board = gameMap.get(code);

        JSONArray jsonArray = new JSONArray();
        for (short s : board) {
            jsonArray.put(s);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("board", jsonArray);
        jsonObject.put("color", color.get(new Pair<>(code, ip)));

        return jsonObject.toString();
    }

    @PostMapping(value = "/chess/board/get_squares", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSquares(@RequestBody String code, @RequestHeader("org") int org) {
        Short[] board = gameMap.get(code);
        short[] squares = Logic.getPossibleMoves(board, org);

        JSONArray jsonArray = new JSONArray();
        for (short s : squares) {
            jsonArray.put(s);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("squares", jsonArray);

        return jsonObject.toString();
    }

    @PostMapping(value = "/chess/board/move", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String move(@RequestBody String code, @RequestHeader("org") int org, @RequestHeader("next") int next, HttpServletRequest request) {
        Short[] board = gameMap.get(code);

        String ip = request.getRemoteAddr();
        if (!color.containsKey(new Pair<>(code, ip))
                || (Math.signum(board[org]) == 1) == color.get(new Pair<>(code, ip))
                || move.get(code) != color.get(new Pair<>(code, ip))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "fail");
            return jsonObject.toString();
        }

        if (Logic.isMoveValid(board, org, next)) {
            Logic.movePiece(board, org, next);

            var current = move.get(code);
            move.remove(code);
            move.put(code, !current);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "succeed");
        return jsonObject.toString();
    }

    @GetMapping(value = "/chess/event")
    public SseEmitter streamDateTime(HttpServletRequest request, @RequestHeader("referer") String referer) {
        String code = "";
        try {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(referer), "UTF-8");
            for (var param : params) {
                if(param.getName().equals("code")) {
                    code = param.getValue();
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String ip = request.getRemoteAddr();
        System.out.println(referer);

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sseEmitter.onCompletion(() -> System.out.println("SseEmitter is completed"));

        sseEmitter.onTimeout(() -> System.out.println("SseEmitter is timed out"));

        sseEmitter.onError((ex) -> System.out.println("SseEmitter got error:" + ex));

        //System.out.println(code + "AA");
        String finalCode = code;
        executor.execute(() -> {
            if (color.containsKey(new Pair<>(finalCode, ip))) {
                while (color.get(new Pair<>(finalCode, ip)) != move.get(finalCode)) {}
                try {
                    sseEmitter.send("hej");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            sseEmitter.complete();
        });

        return sseEmitter;
    }
}
