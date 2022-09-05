window.onload = () => {
    let text = document.getElementById("text")
    let goatcode = document.getElementById("goatcode")
    let defaultGoatCodeFont = parseFloat(window.getComputedStyle(goatcode).fontSize)
    let defaultFont = parseFloat(window.getComputedStyle(text).fontSize)
    document.addEventListener("scroll", (e) => {
        if (window.innerHeight > 800) {
            let size = defaultFont/(window.pageYOffset+1)
            let size2 = Math.max(defaultGoatCodeFont/2, defaultGoatCodeFont-window.pageYOffset)
            if (size < 2) {
                text.style.fontSize = '0px'
                document.getElementById("header").style.height = `${size2*1.2}px`
            } else {
                text.style.fontSize = `${size}px`
                document.getElementById("header").style.height = `${size + size2*1.5}px`
            }
            text.style.opacity = `${100-window.pageYOffset}%`
            goatcode.style.fontSize = `${size2}px`
        }
    })
}
