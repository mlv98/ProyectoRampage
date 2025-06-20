const CACHE_NAME = 'metaverso-cache-v1';
const ASSETS = [
  '/', '/index.html', '/manifest.json', '/styles.css',
  'https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.css',
  'https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.js',
  'https://cdn.tailwindcss.com',
  'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css'
];
self.addEventListener('install', e => {
  e.waitUntil(caches.open(CACHE_NAME).then(c => c.addAll(ASSETS)));
});
self.addEventListener('fetch', e => {
  e.respondWith(
    caches.match(e.request).then(resp => resp || fetch(e.request).then(r => {
      return caches.open(CACHE_NAME).then(cache => { cache.put(e.request, r.clone()); return r; });
    }))
  );
});
