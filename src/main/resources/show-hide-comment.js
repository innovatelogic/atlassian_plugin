// Shows the dialog when the "Show dialog" button is clicked
AJS.$("#dialog-show-button2").click(function() {
    AJS.dialog2("#demo-dialog2").show();
});
// Hides the dialog
AJS.$("#dialog-close-button2").click(function(e) {
    e.preventDefault();
    AJS.dialog2("#demo-dialog2").hide();
});
