const CACHE_NAME = 'tradescore-pwa-v7';
const CORE_ASSETS = [
  '/',
  '/index.html',
  '/manifest.json',
  '/icon-192.svg',
  '/icon-512.svg'
];

// Install: Cache all core assets safely
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      // Use individual caching so if one fails, others still succeed
      return Promise.allSettled(
        CORE_ASSETS.map((asset) =>
          fetch(asset, { cache: 'reload' })
            .then((res) => {
              if (res.ok) {
                return cache.put(asset, res);
              }
            })
            .catch((err) => console.warn('[SW] Skip caching:', asset, err))
        )
      );
    })
  );
  self.skipWaiting();
});

// Activate: Clean up old caches immediately
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            return caches.delete(key);
          }
        })
      );
    })
  );
  self.clients.claim();
});

// Fetch handler
self.addEventListener('fetch', (event) => {
  const req = event.request;

  // Only handle GET requests
  if (req.method !== 'GET') return;

  const url = new URL(req.url);

  // For HTML navigation requests (opening app / page load)
  if (req.mode === 'navigate') {
    event.respondWith(
      fetch(req)
        .then((response) => {
          if (response && response.status === 200) {
            const copy = response.clone();
            caches.open(CACHE_NAME).then((cache) => cache.put(req, copy));
          }
          return response;
        })
        .catch(() => {
          // Offline fallback
          return caches.match('/')
            .then((res) => res || caches.match('/index.html'));
        })
    );
    return;
  }

  // For static assets (images, icons, styles, fonts)
  event.respondWith(
    caches.match(req).then((cachedResponse) => {
      if (cachedResponse) {
        // Fetch in background to revalidate
        fetch(req).then((networkResponse) => {
          if (networkResponse && (networkResponse.status === 200 || networkResponse.type === 'opaque')) {
            caches.open(CACHE_NAME).then((cache) => cache.put(req, networkResponse));
          }
        }).catch(() => {/* ignore background fetch errors */});

        return cachedResponse;
      }

      return fetch(req).then((response) => {
        if (!response || (response.status !== 200 && response.type !== 'opaque')) {
          return response;
        }
        const copy = response.clone();
        caches.open(CACHE_NAME).then((cache) => cache.put(req, copy));
        return response;
      }).catch((err) => {
        // If image or icon, fallback if in cache
        if (req.destination === 'image') {
          return caches.match('/icon-192.svg');
        }
        throw err;
      });
    })
  );
});
