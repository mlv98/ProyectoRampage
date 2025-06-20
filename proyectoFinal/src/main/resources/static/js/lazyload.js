
const lazyImages = document.querySelectorAll('img.lazyload');
const observer = new IntersectionObserver((entries, obs) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      const img = entry.target;
      img.src = img.dataset.src || img.src;
      img.classList.remove('lazyload');
      obs.unobserve(img);
    }
  });
}, { rootMargin: '200px' });

lazyImages.forEach(img => observer.observe(img));