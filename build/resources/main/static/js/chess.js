import { NativeEventSource, EventSourcePolyfill } from 'event-source-polyfill';
let url = window.location.search;
let code = new URLSearchParams(url).get('code');
let board = [0, 0, 0];
let dragging = false;
let draged_piece = 0;
let playing_as = 'white';
const EventSource = NativeEventSource || EventSourcePolyfill;
const eventSource = new EventSource('http://localhost:8080/chess/event/', {
    headers: {
        code: code,
    },
});
eventSource.onmessage = (e) => {
    updateBoard();
};
function idToName(id) {
    let color = '';
    if (id < 0) {
        color = 'white';
    }
    else {
        color = 'black';
    }
    let piece = '';
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
    }).then((response) => response.json().then((data) => {
        board = data['board'];
        if (data['color']) {
            playing_as = 'white';
        }
        else {
            playing_as = 'black';
        }
        fun.call({});
    }));
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
    }).then((response) => {
        response.json().then((data) => {
            const moves = data['squares'];
            console.log(moves);
            const squares = document.getElementsByClassName('square');
            moves.forEach((id, index) => {
                if (id == 2 && board[index] != 0) {
                    squares[index].classList.add('possible');
                }
                else {
                    if (squares[index].classList.contains('possible')) {
                        squares[index].classList.remove('possible');
                    }
                    if (id == 2) {
                        const image = document.createElement('img');
                        image.src = '/images/select.png';
                        image.className = 'indicator';
                        image.style.marginLeft = '-5px';
                        image.style.marginTop = '-5px';
                        squares[index].appendChild(image);
                        console.log(image);
                    }
                    else {
                        const indicators = squares[index].getElementsByClassName('indicator');
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
    const image = document.createElement('img');
    image.src = '/images/' + idToName(id) + '.png';
    image.draggable = false;
    image.className = 'piece select-disable';
    image.addEventListener('click', function (e) {
        if (e.target instanceof HTMLImageElement && !dragging) {
            const id = board[index];
            if ((playing_as == 'white' && id > 0) ||
                (playing_as == 'black' && id < 0)) {
                return;
            }
            // Create piece following cursor
            const piece = document.createElement('img');
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
    loadBoard(() => {
        let squares = document.getElementsByClassName('square');
        board.forEach((id, index) => {
            let square = squares[index];
            const name = idToName(id);
            if (id == 0) {
                square.innerHTML = '';
            }
            else {
                let image = square.getElementsByClassName('piece')[0];
                if (image instanceof HTMLImageElement) {
                    image.src = 'images/' + name + '.png';
                }
                else {
                    square.appendChild(generatePiece(id, index));
                }
            }
            let indicators = square.getElementsByClassName('indicator');
            if (indicators.length > 0) {
                indicators[0].remove();
            }
            square.classList.remove('possible');
        });
    });
}
window.onload = (event) => {
    loadBoard(() => {
        board.forEach((id, index) => {
            if (id == 0)
                return;
            const image = generatePiece(id, index);
            document
                .getElementsByClassName('board')[0]
                .getElementsByClassName('square')[index].appendChild(image);
        });
    });
    let squares = document.getElementsByClassName('square');
    for (var i = 0; i < 64; i++) {
        let square = squares[i];
        let index = i;
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
                }).then((response) => response.json().then((data) => {
                    const statusCode = data['status'];
                    console.log(code);
                    dragging = false;
                    document.body
                        .getElementsByClassName('moving-piece')[0]
                        .remove();
                    updateBoard();
                }));
            }
        });
    }
};
document.onmousemove = (event) => {
    if (dragging) {
        let piece = document.body.getElementsByClassName('moving-piece')[0];
        if (piece instanceof HTMLImageElement) {
            piece.style.left = (event.clientX - 50).toString() + 'px';
            piece.style.top = (event.clientY - 50).toString() + 'px';
        }
    }
};
