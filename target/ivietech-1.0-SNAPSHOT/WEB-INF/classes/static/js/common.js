/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function loadMore(page, type, platform, bestproduct, bestSell, promotion, position1,position2) {
    $.ajax({
        method: 'get',
        url: '/api/detail',
        data: {
            type: type,
            page: page,
            platform: platform,
            bestproduct: bestproduct,
            bestsell: bestSell,
            promotion: promotion
        },
        success: function (data) {
            var po = '.' + position1 + ' .row';
            $(po).append(data);
            if (data.length<100) {
                $(position2).addClass("hidden");
            }
        }
    })
}

$(document).ready(function () {

    $("#home-cart").hover(
            function () {
                $('#dropdown-detail-cart').css("display", "block");
            }, function () {
        $('#dropdown-detail-cart').css("display", "none");
    }
    );

    $(".home-mini-profile").hover(
            function () {
                $('.drop-mini-profile').css("display", "block");
            }, function () {
        $('.drop-mini-profile').css("display", "none");
    }
    );

});

$(document).ready(function () {
    $('#dismiss, .overlay').on('click', function () {
        $('#sidebar').removeClass('active');
        $('.overlay').removeClass('active');
        $('body').css('position', 'unset');
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').addClass('active');
        $('.overlay').addClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
        $('body').css('position', 'fixed');
    });
});
function getInfoRecharge() {
    $.ajax({
        url: "http://localhost:8080" + "/user/recharge/info",
        type: "get",
        data: {
        }
        ,
        success: function (event) {
            if (event.status === "Đang đợi") {
                $("#status-check").text("Đang đợi chuyển khoản");
                $("#loader").attr("style", "");
            } else if (event.status === "Thành công") {
                $("#loader").attr("style", "display: none");
                $("#status-check").text("Thành công");
                $("#status-check").attr("class", "alert alert-success");

            } else {
                $("#loader").attr("style", "display: none");
                $("#status-check").text("Thất bại do quá thời gian chuyển khoản");
                $("#status-check").attr("class", "alert alert-danger");
            }

        }
    })
}