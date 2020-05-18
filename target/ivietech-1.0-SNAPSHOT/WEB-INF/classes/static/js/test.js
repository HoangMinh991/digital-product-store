/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var addCartBtn = document.getElementById('add-cart-btn');
var product_name = document.getElementsByClassName('product_name');
addCartBtn.addEventListener('click', addToCart);

var session_data = '@Session["userID"]'
function addToCart(){
   console.log(addCartBtn);
   console.log(product_name);
}