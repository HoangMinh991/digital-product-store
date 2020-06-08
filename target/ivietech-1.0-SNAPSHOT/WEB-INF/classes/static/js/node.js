/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$("#category-product").hover(
        function () {
            $(this).css("cursor", "pointer");
            $('.drop-category').css("display", "grid");
        }, function () {
    $('.drop-category').css("display", "none");
}
);