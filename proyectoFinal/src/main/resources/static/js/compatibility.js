


document.addEventListener('DOMContentLoaded', () => {
  const rightPanel = document.querySelector('.right-panel');
  const overlay = document.getElementById('loadingOverlay');
  const statusText = overlay ? overlay.querySelector('p') : null;

  // Oculta inicialmente el panel derecho y el overlay
  if (rightPanel) {
    rightPanel.style.display = 'none';
    rightPanel.style.opacity = '0';
    rightPanel.style.transition = 'opacity 6s ease';
  }
  if (overlay) {
    overlay.style.display = 'none';
    overlay.style.opacity = '0';
    overlay.style.transition = 'opacity 6s ease';
  }

  // Helper debounce
  const debounce = (fn, delay = 300) => {
    let timer;
    return (...args) => {
      clearTimeout(timer);
      timer = setTimeout(() => fn.apply(this, args), delay);
    };
  };

  // Autocomplete genérico
  function setupAutocomplete(inpId, sugId, url, selectFn) {
    const inp = document.getElementById(inpId);
    const sug = document.getElementById(sugId);
    if (!inp || !sug) return;

    inp.addEventListener('input', debounce(() => {
      const q = inp.value.trim();
      if (q.length < 2) { sug.innerHTML = ''; return; }
      fetch(`${url}?query=${encodeURIComponent(q)}`)
        .then(res => res.json())
        .then(data => {
          sug.innerHTML = '';
          data.forEach(item => {
            const div = document.createElement('div');
            div.textContent = item.name;
            div.className = 'p-2 hover:bg-gray-600 cursor-pointer';
            div.addEventListener('click', () => selectFn(item));
            sug.appendChild(div);
          });
        });
    }, 300));

    document.addEventListener('click', e => {
      if (!sug.contains(e.target) && e.target.id !== inpId) sug.innerHTML = '';
    });
  }

  // Inicializa autocompletados
  setupAutocomplete('gameName', 'gameSuggestions', '/compatibility/search', item => {
    document.getElementById('gameName').value = item.slug;
    document.getElementById('gameSuggestions').innerHTML = '';
  });
  setupAutocomplete('gpuName', 'gpuSuggestions', '/compatibility/search/gpus', item => {
    document.getElementById('gpuName').value = item.name;
    document.getElementById('gpuId').value = item.id;
    document.getElementById('gpuSuggestions').innerHTML = '';
  });
  setupAutocomplete('cpuName', 'cpuSuggestions', '/compatibility/search/cpus', item => {
    document.getElementById('cpuName').value = item.name;
    document.getElementById('cpuId').value = item.id;
    document.getElementById('cpuSuggestions').innerHTML = '';
  });

  // Inicializa AOS si está disponible
  if (window.AOS) AOS.init();

  // Maneja el submit para mostrar spinner y retrasar envío
  const form = document.querySelector('form.compat-form');
  if (form && overlay) {
    form.addEventListener('submit', e => {
      const cpuIdVal = document.getElementById('cpuId').value;
      if (!cpuIdVal) {
        alert('Selecciona una CPU.');
        e.preventDefault();
        return;
      }
      // Prevenir envío inmediato
      e.preventDefault();
      // Mostrar overlay con spinner
      overlay.style.display = 'flex';
      requestAnimationFrame(() => overlay.style.opacity = '1');
      if (statusText) statusText.textContent = 'Analizando compatibilidad...';
      // Esperar 5 segundos antes de enviar el formulario
      setTimeout(() => {
        form.submit();
      }, 5000);
    });
  }

  // Al cargar la página con resultados, muestra panel y gráfica
  window.addEventListener('load', () => {
    if (overlay) {
      overlay.style.opacity = '0';
      overlay.addEventListener('transitionend', () => {
        overlay.style.display = 'none';
      }, { once: true });
    }
    if (rightPanel) {
      rightPanel.style.display = 'block';
      requestAnimationFrame(() => rightPanel.style.opacity = '1');
    }
    if (window.resultData) renderChart(window.resultData);
  });
});

// Dibuja la gráfica con Chart.js
function renderChart(data) {
  const canvas = document.getElementById('compatChart');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['CPU','GPU','RAM'],
      datasets: [
        { label: 'Tu PC',       data: [data.cpuUser, data.gpuUser, data.ramUser], backgroundColor: 'rgba(14,165,233,0.8)' },
        { label: 'Mínimo',      data: [data.cpuMin,  data.gpuMin,  data.ramMin], backgroundColor: 'rgba(107,33,168,0.8)' },
        { label: 'Recomendado', data: [data.cpuRec,  data.gpuRec,  data.ramRec], backgroundColor: 'rgba(164,63,234,0.8)' }
      ]
    },
    options: {
      responsive: true,
      plugins: { legend: { position: 'bottom' } },
      animation: { duration: 6000 },
      scales: { y: { beginAtZero: true } }
    }
  });
}
