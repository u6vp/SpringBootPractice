var deleteButton = document.getElementById("deleteLink");
var dialog = document.querySelector("dialog");

// モーダルダイアログの表示（背景暗くなる）
deleteButton.addEventListener("click", function() {
  dialog.showModal();
}, false);

dialog.addEventListener("cancel", function(event){
  event.preventDefault();
});

// dialog.addEventListener("close", function(event){
//   if (this.returnValue !== "cancel") {
//     var deleteBilling = document.getElementById("delete").value;
//     window.location.href = deleteBilling;
//   }
// });