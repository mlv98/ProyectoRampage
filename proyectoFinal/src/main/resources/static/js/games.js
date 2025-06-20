

document.addEventListener("DOMContentLoaded", () => {
  if (typeof AOS !== 'undefined') AOS.init({ duration: 600, once: true });

  const container = document.getElementById('gamesContainer');
  let debounce;

  async function fetchGames(query = '') {
    try {
      const url = `/api/games?search=${encodeURIComponent(query)}`;
      const { data } = await axios.get(url);
      container.innerHTML = '';
      data.results.forEach(game => renderGameCard(game));
    } catch (error) {
      console.error('Error cargando juegos:', error);
    }
  }

  function renderGameCard(game) {
    // 1) wrapper relative (para botón)
    const wrapper = document.createElement('div');
    wrapper.className = 'relative';
    wrapper.setAttribute('data-aos', 'fade-up');

    // 2) link
    const link = document.createElement('a');
    link.href = `/games/${game.slug}`;
    link.className = 'flex flex-col h-96 bg-gray-800 rounded-xl shadow-lg overflow-hidden hover:shadow-2xl transition';

    // 3) imagen
    const img = document.createElement('img');
    img.src = game.background_image;
    img.alt = game.name;
    img.className = 'h-1/2 w-full object-cover';
    link.appendChild(img);

    // 4) cuerpo
    const body = document.createElement('div');
    body.className = 'flex-1 p-4 flex flex-col justify-between relative';
    const h2 = document.createElement('h2');
    h2.textContent = game.name;
    h2.className = 'text-xl font-semibold text-white';
    body.appendChild(h2);
    const p = document.createElement('p');
    p.className = 'text-sm text-gray-400 flex flex-wrap gap-1 mt-2';
    game.platforms.forEach(pl => {
      const span = document.createElement('span');
      span.textContent = pl.platform.name;
      span.className = 'px-2 py-1 bg-gray-700 rounded-full';
      p.appendChild(span);
    });
    body.appendChild(p);

    link.appendChild(body);
    wrapper.appendChild(link);

    // 5) botón favorito
    const favBtn = document.createElement('button');
    favBtn.type = 'button';
    favBtn.className = 'fav-btn absolute top-2 right-2 text-2xl z-30';
    const icon = document.createElement('i');
    const isFav = favoriteIds.includes(game.id);
    icon.className = `fa-heart ${isFav ? 'fa-solid text-red-500' : 'fa-regular text-white'}`;
    favBtn.appendChild(icon);

    // 6) evento toggle favorito
    favBtn.addEventListener('click', async e => {
      e.preventDefault();
      const url = isFav ? '/favorites/remove' : '/favorites/add';
      try {
        const res = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
          },
          body: new URLSearchParams({ gameId: game.id })
        });
        if (!res.ok) throw new Error('status ' + res.status);
        icon.classList.toggle('fa-solid');
        icon.classList.toggle('fa-regular');
        icon.classList.toggle('text-red-500');
        icon.classList.toggle('text-white');
        if (isFav) {
          favoriteIds.splice(favoriteIds.indexOf(game.id), 1);
        } else {
          favoriteIds.push(game.id);
        }
      } catch (err) {
        console.error('Error toggling favorite:', err);
      }
    });

    wrapper.appendChild(favBtn);
    container.appendChild(wrapper);
  }

  // listener de búsqueda en TODOS los inputs con id="searchInput"
  const searchInputs = document.querySelectorAll('#searchInput');
  searchInputs.forEach(input => {
    input.addEventListener('input', e => {
      clearTimeout(debounce);
      debounce = setTimeout(() => fetchGames(e.target.value), 500);
    });
  });

  // carga inicial
  fetchGames();
});


// mobile-menu.js: Toggle mobile navigation

document.addEventListener("DOMContentLoaded", function () {
  const toggleBtn = document.getElementById("menu-toggle");
  const mobileMenu = document.getElementById("mobile-menu");

  toggleBtn.addEventListener("click", () => {
    mobileMenu.classList.toggle("hidden");
  });
});


// toggle-avatar.js
document.addEventListener("DOMContentLoaded", () => {
  // Recogemos todos los elementos con id="user-menu-button"
  const avatarBtns = document.querySelectorAll("#user-menu-button");
  const dropdown   = document.getElementById("user-dropdown");

  avatarBtns.forEach(btn => {
    btn.addEventListener("click", (e) => {
      e.stopPropagation();            // evita cierres accidentales
      dropdown.classList.toggle("hidden");
    });
  });

  // Cerrar al click fuera
  document.addEventListener("click", (e) => {
    if (!dropdown.classList.contains("hidden") 
        && !e.target.closest("#user-menu-button") 
        && !e.target.closest("#user-dropdown")) {
      dropdown.classList.add("hidden");
    }
  });
});
