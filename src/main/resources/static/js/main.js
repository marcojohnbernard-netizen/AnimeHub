// Maliit na progressive enhancement - hindi kailangan para gumana ang site,
// pero mas magandang experience kapag may JS.

document.addEventListener('DOMContentLoaded', function () {
    // Kumpirmahin bago alisin sa watchlist - iwas accidental clicks
    document.querySelectorAll('.btn-remove').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            const confirmed = window.confirm('Sigurado ka bang aalisin ito sa list mo?');
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });
});
