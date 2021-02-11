const selectElement = document.getElementById("select_app");

// 入力条件を定義
selectElement.addEventListener("change", (event) => {
  const inputElement = document.getElementById("new_app");
  if (event.target.value < 0) {
    inputElement.disabled = true;
  } else {
    inputElement.disabled = false;
  }
});
