// Shows the dialog when the "Show dialog" button is clicked
AJS.$("#dialog-show-button").click(function() {
    AJS.dialog2("#demo-dialog").show();
});
// Hides the dialog
AJS.$("#dialog-close-button").click(function(e) {
    e.preventDefault();
    AJS.dialog2("#demo-dialog").hide();
});
