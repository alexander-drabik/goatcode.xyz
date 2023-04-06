"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var websocket_ts_1 = require("websocket-ts");
var url = window.location.search;
var code = new URLSearchParams(url).get('code');
var board = [0, 0, 0];
var dragging = false;
var draged_piece = 0;
var playing_as = 'white';
function idToName(id) {
    var color = '';
    if (id < 0) {
        color = 'white';
    }
    else {
        color = 'black';
    }
    var piece = '';
    switch (Math.abs(id)) {
        case 1:
            piece = 'king';
            break;
        case 2:
            piece = 'queen';
            break;
        case 3:
            piece = 'bishop';
            break;
        case 4:
            piece = 'knight';
            break;
        case 5:
            piece = 'rook';
            break;
        case 6:
            piece = 'pawn';
            break;
    }
    return color + '_' + piece;
}
function loadBoard(fun) {
    fetch('http://localhost:8080/chess/board/get/', {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        body: code,
    }).then(function (response) {
        return response.json().then(function (data) {
            board = data['board'];
            if (data['color']) {
                playing_as = 'white';
            }
            else {
                playing_as = 'black';
            }
            fun.call({});
        });
    });
}
function loadSquares() {
    fetch('http://localhost:8080/chess/board/get_squares/', {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        body: code,
        headers: {
            org: draged_piece.toString(),
        },
    }).then(function (response) {
        response.json().then(function (data) {
            var moves = data['squares'];
            console.log(moves);
            var squares = document.getElementsByClassName('square');
            moves.forEach(function (id, index) {
                if (id == 2 && board[index] != 0) {
                    squares[index].classList.add('possible');
                }
                else {
                    if (squares[index].classList.contains('possible')) {
                        squares[index].classList.remove('possible');
                    }
                    if (id == 2) {
                        var image = document.createElement('img');
                        image.src = '/images/select.png';
                        image.className = 'indicator';
                        image.style.marginLeft = '-5px';
                        image.style.marginTop = '-5px';
                        squares[index].appendChild(image);
                        console.log(image);
                    }
                    else {
                        var indicators = squares[index].getElementsByClassName('indicator');
                        if (indicators.length > 0) {
                            indicators[0].remove();
                        }
                    }
                }
            });
        });
    });
}
function generatePiece(id, index) {
    var image = document.createElement('img');
    image.src = '/images/' + idToName(id) + '.png';
    image.draggable = false;
    image.className = 'piece select-disable';
    image.addEventListener('click', function (e) {
        if (e.target instanceof HTMLImageElement && !dragging) {
            var id_1 = board[index];
            if ((playing_as == 'white' && id_1 > 0) ||
                (playing_as == 'black' && id_1 < 0)) {
                return;
            }
            // Create piece following cursor
            var piece = document.createElement('img');
            piece.className = 'moving-piece select-disable';
            piece.draggable = false;
            piece.src = e.target.src;
            piece.style.left = (e.clientX - 50).toString() + 'px';
            piece.style.top = (e.clientY - 50).toString() + 'px';
            document.body.appendChild(piece);
            e.target.remove();
            dragging = true;
            draged_piece = index;
            loadSquares();
        }
    });
    return image;
}
function updateBoard() {
    loadBoard(function () {
        var squares = document.getElementsByClassName('square');
        board.forEach(function (id, index) {
            var square = squares[index];
            var name = idToName(id);
            if (id == 0) {
                square.innerHTML = '';
            }
            else {
                var image = square.getElementsByClassName('piece')[0];
                if (image instanceof HTMLImageElement) {
                    image.src = 'images/' + name + '.png';
                }
                else {
                    square.appendChild(generatePiece(id, index));
                }
            }
            var indicators = square.getElementsByClassName('indicator');
            if (indicators.length > 0) {
                indicators[0].remove();
            }
            square.classList.remove('possible');
        });
    });
}
window.onload = function (event) {
    loadBoard(function () {
        board.forEach(function (id, index) {
            if (id == 0)
                return;
            var image = generatePiece(id, index);
            document
                .getElementsByClassName('board')[0]
                .getElementsByClassName('square')[index].appendChild(image);
        });
    });
    var squares = document.getElementsByClassName('square');
    var _loop_1 = function () {
        var square = squares[i];
        var index = i;
        square.addEventListener('click', function (e) {
            if (dragging && index != draged_piece) {
                fetch('http://localhost:8080/chess/board/move/', {
                    method: 'POST',
                    mode: 'cors',
                    cache: 'no-cache',
                    body: code,
                    headers: {
                        org: draged_piece.toString(),
                        next: index.toString(),
                    },
                }).then(function (response) {
                    return response.json().then(function (data) {
                        var statusCode = data['status'];
                        console.log(code);
                        dragging = false;
                        document.body
                            .getElementsByClassName('moving-piece')[0]
                            .remove();
                        updateBoard();
                    });
                });
            }
        });
    };
    for (var i = 0; i < 64; i++) {
        _loop_1();
    }
};
document.onmousemove = function (event) {
    if (dragging) {
        var piece = document.body.getElementsByClassName('moving-piece')[0];
        if (piece instanceof HTMLImageElement) {
            piece.style.left = (event.clientX - 50).toString() + 'px';
            piece.style.top = (event.clientY - 50).toString() + 'px';
        }
    }
};
var ws = new websocket_ts_1.WebsocketBuilder('ws://localhost:42421')
    .onOpen(function (i, ev) {
    console.log('open');
})
    .onMessage(function (i, e) {
    console.log('echo sent');
})
    .build();
