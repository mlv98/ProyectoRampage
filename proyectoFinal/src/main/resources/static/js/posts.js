
document.addEventListener('DOMContentLoaded', () => {
  // --- Configurar CSRF para Axios ---
  const token = document.querySelector('meta[name="_csrf"]').content;
  const header = document.querySelector('meta[name="_csrf_header"]').content;
  axios.defaults.headers.common[header] = token;
  

  // --- LIKE BUTTONS ---
  document.querySelectorAll('.btn-like').forEach(btn => {
    const icon      = btn.querySelector('.icon');       
    const countElem = btn.querySelector('.count');     

    // Estado inicial basado en si ya trae la clase 'liked'
    const inicialLiked = btn.classList.contains('liked');
    btn.setAttribute('aria-pressed', inicialLiked);
    icon.classList.toggle('text-red-500', inicialLiked);

    btn.addEventListener('click', async e => {
      e.preventDefault();
      const postId = btn.dataset.postId;
      try {
        const resp = await axios.post(`/posts/${postId}/like`);
        const newCount = resp.data.count;

        // Actualiza el contador de likes en el DOM
        countElem.textContent = newCount;

        // Calcula el nuevo estado (invierte el actual)
        const nuevoEstado = btn.getAttribute('aria-pressed') === 'false';

        // Aplica al botón y al icono
        btn.setAttribute('aria-pressed', nuevoEstado);
        btn.classList.toggle('liked', nuevoEstado);
        icon.classList.toggle('fa-solid', nuevoEstado);
        icon.classList.toggle('fa-regular', !nuevoEstado);
        icon.classList.toggle('text-red-500', nuevoEstado);
      } catch (err) {
        console.error('Error al dar like:', err);
      }
    });
  });

  // --- TOGGLE DE COMENTARIOS ---
  document.querySelectorAll('.btn-comment').forEach(btn => {
    btn.addEventListener('click', e => {
      e.preventDefault();
      const postCard = btn.closest('.post-card');
      const comments = postCard.querySelector('.comments-section');
      if (!comments) return;
      const isHidden = comments.hasAttribute('hidden');
      comments.toggleAttribute('hidden');
      btn.setAttribute('aria-expanded', isHidden);
    });
  });

  // --- VIDEO LAZY‑LOAD ---
  const vids = document.querySelectorAll('video[preload="none"]');
  if ('IntersectionObserver' in window) {
    const obs = new IntersectionObserver((entries, obsr) => {
      entries.forEach(ent => {
        if (ent.isIntersecting) {
          const v = ent.target;
          v.preload = 'metadata';
          obsr.unobserve(v);
        }
      });
    }, { threshold: 0.25 });
    vids.forEach(v => obs.observe(v));
  } else {
    vids.forEach(v => v.preload = 'metadata');
  }


  document.querySelectorAll('.post-media video').forEach(video => {
    video.addEventListener('mouseenter', () => video.play().catch(()=>{}));
    video.addEventListener('mouseleave', () => {
      video.pause();
      video.currentTime = 0;
    });
  });
});

const form = document.querySelector('form');
if (form) {
  form.addEventListener('submit', async e => {
    e.preventDefault();
    const data = new FormData(form);
    await axios.post(form.action, data);
    // una vez completado el POST, fuerza el redirect:
    window.location.href = '/posts';
  });
}


document.addEventListener('DOMContentLoaded', function() {
     document.querySelectorAll('.edit-btn').forEach(btn => {
       btn.addEventListener('click', e => {
         e.preventDefault();
         const card = btn.closest('li');
         card.querySelector('.view-mode').classList.add('hidden');
         card.querySelector('.edit-form').classList.remove('hidden');
       });
     });
     document.querySelectorAll('.cancel-edit').forEach(btn => {
       btn.addEventListener('click', e => {
         const card = btn.closest('li');
         card.querySelector('.edit-form').classList.add('hidden');
         card.querySelector('.view-mode').classList.remove('hidden');
       });
     });
   });
