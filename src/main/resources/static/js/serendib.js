/*
 * Serendib Central -- the site's custom JavaScript. Two small pieces:
 *
 * 1. The "mall directory" on the landing page: a saffron lift car moves from
 *    floor to floor (each floor = a product category) every few seconds, and
 *    the little display at the top shows the current level.
 * 2. The status board on the role home pages: its numbers count up once
 *    when the page loads (see the bottom of this file).
 *
 * Pages without these simply skip them.
 */
document.addEventListener("DOMContentLoaded", function () {
    var board = document.querySelector("[data-directory]");
    if (!board) {
        return;
    }

    var floors = board.querySelectorAll(".floor");
    var car = board.querySelector(".directory__car");
    var levelText = board.querySelector("[data-lift-level]");
    var arrow = board.querySelector("[data-lift-arrow]");
    if (floors.length === 0 || !car) {
        return;
    }

    var current = 0;
    var paused = false;

    // Move the lift car to a floor, update the display, highlight the floor.
    function goTo(index) {
        // Higher index = higher floor, so moving to a lower index means going down.
        var goingDown = index < current;
        floors[current].classList.remove("is-current");
        current = index;
        floors[current].classList.add("is-current");
        car.style.transform = "translateY(" + floors[current].offsetTop + "px)";
        levelText.textContent = floors[current].getAttribute("data-level");
        arrow.classList.toggle("is-down", goingDown);
    }

    goTo(0);

    // Respect the OS "reduce motion" setting: show the board, but keep it still.
    var reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion) {
        return;
    }

    // Boot sequence (CSS animation), then start the lift once it has finished.
    board.classList.add("is-booting");
    var bootTime = floors.length * 90 + 900;

    setTimeout(function () {
        setInterval(function () {
            if (!paused) {
                goTo((current + 1) % floors.length);
            }
        }, 2400);
    }, bootTime);

    // When a person points at or tabs to a floor, the lift goes there and waits.
    floors.forEach(function (floor, index) {
        floor.addEventListener("mouseenter", function () { paused = true; goTo(index); });
        floor.addEventListener("focusin", function () { paused = true; goTo(index); });
    });
    board.addEventListener("mouseleave", function () { paused = false; });
    board.addEventListener("focusout", function () { paused = false; });
});

/*
 * Status board on the role home pages. The page already prints the real
 * numbers, so without JavaScript (or with "reduce motion") the board is
 * simply correct and still. Otherwise each plate switches on in turn (CSS)
 * and its number counts up from 0 to the real value.
 */
document.addEventListener("DOMContentLoaded", function () {
    var board = document.querySelector("[data-board]");
    if (!board || window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
        return;
    }

    board.classList.add("is-booting");

    board.querySelectorAll(".board__item").forEach(function (item, index) {
        // Plates showing text instead of a number (e.g. "New" rating) don't count up.
        var plate = item.querySelector("[data-count]");
        if (!plate) {
            return;
        }
        var text = plate.getAttribute("data-count");
        var target = parseFloat(text);
        // Keep the same number of decimals as the real value (e.g. a 4.5 rating).
        var decimals = text.indexOf(".") === -1 ? 0 : text.length - text.indexOf(".") - 1;
        var duration = 700;
        // Start when this plate's switch-on animation starts (same delay as the CSS).
        var delay = index * 90 + 150;

        plate.textContent = (0).toFixed(decimals);

        setTimeout(function () {
            var start = null;
            function step(now) {
                if (start === null) {
                    start = now;
                }
                var progress = Math.min((now - start) / duration, 1);
                var eased = 1 - Math.pow(1 - progress, 3); // fast at first, then settles
                plate.textContent = (target * eased).toFixed(decimals);
                if (progress < 1) {
                    requestAnimationFrame(step);
                }
            }
            requestAnimationFrame(step);
        }, delay);
    });
});
