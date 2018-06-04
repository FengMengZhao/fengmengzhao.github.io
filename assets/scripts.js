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

/* 搜素事件  */
SimpleJekyllSearch({
    searchInput: document.getElementById('search-input'),
    resultsContainer: document.getElementById('results-container'),
    json: '/search.json',
    searchResultTemplate: '<li><a href="{url}" title="{desc}">{title}</a></li>',
    noResultsText: '没有搜索到文章',
    limit: 20,
    fuzzy: false
})

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

/* 百度统计 */
<script>
    var _hmt = _hmt || [];
    (function() {
      var hm = document.createElement("script");
      hm.src = "https://hm.baidu.com/hm.js?a66e23d778dbfa813dcc8e5ad7b661ec";
      var s = document.getElementsByTagName("script")[0]; 
      s.parentNode.insertBefore(hm, s);
    })();
</script>
