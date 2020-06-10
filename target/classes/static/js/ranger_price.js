/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function() {
    var parent = document.querySelector(".price-slider");
    if(!parent) return;

    var
        rangeS = parent.querySelectorAll(".input-price-range"),
        numberS = parent.querySelectorAll(".input-price-number");

    rangeS.forEach(function(el) {
        el.oninput = function() {
            var slide1 = Math.floor(rangeS[0].value.replace(/\./g, '')),
                slide2 = Math.floor(rangeS[1].value.replace(/\./g, ''));

            if (slide1 > slide2) {
                [slide1, slide2] = [slide2, slide1];
            }

            numberS[0].value = formatNumber(slide1);
            numberS[1].value = formatNumber(slide2);
        }
    });

    numberS.forEach(function(el) {
        el.oninput = function() {
            var number1 = Math.floor(numberS[0].value.replace(/\./g, '')),
                number2 = Math.floor(numberS[1].value.replace(/\./g, ''));

            // if (number1 > number2) {
            //     var tmp = number1;
            //     numberS[0].value = formatNumber(number2);
            //     numberS[1].value = formatNumber(tmp);
            // }

            rangeS[0].value = number1;
            rangeS[1].value = number2;

        }
    });
})();

function formatNumber(n) {
    return String(n).replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}


