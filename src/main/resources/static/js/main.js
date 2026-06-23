// Small progressive enhancements - the site still works without JS,
// but these make the experience smoother.

document.addEventListener('DOMContentLoaded', function () {

    // ----- Hamburger menu toggle -----
    var menuBtn = document.getElementById('menuToggleBtn');
    var menuPanel = document.getElementById('menuPanel');
    if (menuBtn && menuPanel) {
        menuBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            var isOpen = menuPanel.classList.toggle('open');
            menuBtn.setAttribute('aria-expanded', isOpen);
        });
        document.addEventListener('click', function (e) {
            if (!menuPanel.contains(e.target) && e.target !== menuBtn) {
                menuPanel.classList.remove('open');
                menuBtn.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // ----- Search drawer toggle -----
    var searchBtn = document.getElementById('searchToggleBtn');
    var searchDrawer = document.getElementById('searchDrawer');
    if (searchBtn && searchDrawer) {
        searchBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            var isOpen = searchDrawer.classList.toggle('open');
            searchBtn.setAttribute('aria-expanded', isOpen);
            if (isOpen) {
                var input = searchDrawer.querySelector('input');
                if (input) input.focus();
            }
        });
    }

    // ----- Confirm before removing from watchlist -----
    document.querySelectorAll('.btn-remove').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            var confirmed = window.confirm('Remove this anime from your list?');
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });
});
