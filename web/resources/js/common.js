/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function changeClass(elementID, newClass) {

    var element = document.getElementById(elementID);
    element.setAttribute("class", newClass);
    element.setAttribute("className", newClass);

}