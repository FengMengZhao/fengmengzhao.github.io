$(function () {
  $('[data-toggle="tooltip"]').tooltip()

})
/* 回顶部时间 */
$('body').prepend('<a href="#" class="back-to-top">Back to Top</a>');

var amountScrolled = 300;

$(window).scroll(function() {
  if ( $(window).scrollTop() > amountScrolled  ) {
            //$('a.back-to-top').fadeIn('slow');
            $('a.back-to-top').show();
                
  } else {
            //$('a.back-to-top').fadeOut('slow');
            $('a.back-to-top').hide();
                
  }

});

$('a.back-to-top').click(function() {
  $('html, body').animate({
            scrollTop: 0
                    
  }, 700);
    return false;

});

/* 点击搜索按钮弹出搜索框 */
$('#search-bar').click(function() {
    var $search = $('#search-container');
    if($search.css('display') == 'none'){
        $search.find('input').val('');
        $search.find('ul').empty();
        $search.show();
    }else{
        $search.hide();
    }
});

$('.question_button').click(function() {
  var $this = $('.text_container');

  if ($this.hasClass("hidden")) {
    $this.removeClass("hidden").addClass("visible");
            $this.addClass("text_container_border");

  } else {
    $this.removeClass("visible").addClass("hidden");
            $this.removeClass("text_container_border");
  }
});
