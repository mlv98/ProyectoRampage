document.addEventListener('DOMContentLoaded', () => {
  // inicializa AOS
  AOS.init({ duration: 700, once: true });

  // flechas de scroll
  document.querySelectorAll('.carousel-container').forEach(container => {
    const track   = container.querySelector('.carousel');
    const btnPrev = container.querySelector('.arrow-btn:first-of-type');
    const btnNext = container.querySelector('.arrow-btn:last-of-type');
    btnPrev?.addEventListener('click', () =>
      track.scrollBy({ left: -track.clientWidth * 0.8, behavior: 'smooth' })
    );
    btnNext?.addEventListener('click', () =>
      track.scrollBy({ left:  track.clientWidth * 0.8, behavior: 'smooth' })
    );
  });

  // CSRF tokens
  const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

  // favoritos in‐place
  document.querySelectorAll('.fav-btn').forEach(btn => {
    btn.addEventListener('click', async e => {
      e.preventDefault();
      const card    = btn.closest('.carousel-item');
      const gameId  = card.dataset.gameId;
      const icon    = btn.querySelector('i');
      const isFav   = icon.classList.contains('fa-solid');
      const url     = isFav ? '/favorites/remove' : '/favorites/add';

      try {
        const res = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
          },
          body: new URLSearchParams({ gameId })
        });
        if (res.ok) {
          icon.classList.toggle('fa-solid');
          icon.classList.toggle('fa-regular');
          icon.classList.toggle('text-red-500');
          icon.classList.toggle('text-white');
        }
      } catch (err) {
        console.error('Error toggling favorite:', err);
      }
    });
  });
});

document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.delete-fav-form').forEach(form => {
    form.addEventListener('submit', async e => {
      e.preventDefault();             // ya no recargamos la página
      const card = form.closest('.bg-gray-800'); // o el selector de tu tarjeta
      const data = new URLSearchParams(new FormData(form));
      try {
        const res = await fetch(form.action, {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: data
        });
        if (res.ok) {
          // remueve la tarjeta del DOM
          card.remove();
        } else {
          console.error('Error eliminando favorito', await res.text());
        }
      } catch (err) {
        console.error('Error en fetch:', err);
      }
    });
  });
});



