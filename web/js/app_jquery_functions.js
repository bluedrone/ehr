$.fn.selectContentEditableText = function() {
  var doc = document;
  var element = this[0];
  if (doc.body.createTextRange) {
    var range = document.body.createTextRange();
    range.moveToElementText(element);
    range.select();
  } 
  else if (window.getSelection) {
    var selection = window.getSelection();        
    var range = document.createRange();
    range.selectNodeContents(element);
    selection.removeAllRanges();
    selection.addRange(range);
  }
};

/*
// possibly useful contenteditable change event trigger
$('body').on('focus', '[contenteditable]', function() {
  var $this = $(this);
  $this.data('before', $this.html());
  return $this;
}).on('blur keyup paste input', '[contenteditable]', function() {
  var $this = $(this);
  if ($this.data('before') !== $this.html()) {
    $this.data('before', $this.html());
    $this.trigger('change');
  }
  return $this;
});
*/