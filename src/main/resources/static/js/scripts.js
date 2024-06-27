/*!
* Start Bootstrap - Modern Business v5.0.6 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2022 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project

document.addEventListener("DOMContentLoaded", function() {

  var guestLoginForm = document.getElementByCgetElementsByClassName("login-form");
  var guestNameInput = document.getElementById("username");
  var guestEmailInput = document.getElementById("password");
  var guestLoginButton = document.getElementById("guestLoginButton");
  var subHandle = document.getElementById("subHandle");


  function fillGuestInfo() {
    guestNameInput.value = "kenneth";
    guestEmailInput.value = "password";
  }

  
  guestLoginButton.addEventListener("click", function(event) {
    event.preventDefault(); // Prevent the form from submitting by default
    fillGuestInfo(); 
    guestLoginForm.submit();
  });
  
  // This function toggles the subscription
  function toggleSubStatus() {
	if (subHandle.innerText === "Subscribe") {
		subHandle.innerText === "Unsubscribe";
	} else {
		subHandle.innerText === "Subscribe";
	}
	
	$.get("/clubs/subscription").done;
  }
  
 // subHandle.addEventListener("click", toggleSubStatus);
  
});