const CACHE_NAME = 'tradescore-pwa-v12';
const CORE_ASSETS = [
  '/',
  '/index.html',
  '/manifest.json',
  '/icon-192.svg',
  '/icon-512.svg',
  '/mr_nobody_logo.jpg',
  '/mr_nobody_trader_thumb.jpg',
  '/mr_nobody_trader_512.jpg',
  '/mr_nobody_trader.jpg',
  '/trading_banner_hero.jpg',
  '/trading_banner_analytics.jpg',
  '/trading_banner_journal.jpg'
];

// Install: Cache all core assets safely
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
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

// Activate: Clean up all previous caches immediately
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            console.log('[SW] Purging outdated cache:', key);
            return caches.delete(key);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

// Message listener for immediate updates
self.addEventListener('message', (event) => {
  if (event.data && (event.data === 'skipWaiting' || event.data.type === 'SKIP_WAITING' || event.data.action === 'skipWaiting')) {
    self.skipWaiting();
  }
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
        // Revalidate in background
        fetch(req).then((networkResponse) => {
          if (networkResponse && (networkResponse.status === 200 || networkResponse.type === 'opaque')) {
            caches.open(CACHE_NAME).then((cache) => cache.put(req, networkResponse));
          }
        }).catch(() => {});

        return cachedResponse;
      }

      // Check cache by filename fallback for deep links (e.g., /journal/trading_banner_hero.jpg -> /trading_banner_hero.jpg)
      const filename = '/' + url.pathname.split('/').pop();
      return caches.match(filename).then((fallbackMatch) => {
        if (fallbackMatch) {
          return fallbackMatch;
        }

        return fetch(req).then((response) => {
          if (!response || (response.status !== 200 && response.type !== 'opaque')) {
            return response;
          }
          const copy = response.clone();
          caches.open(CACHE_NAME).then((cache) => cache.put(req, copy));
          return response;
        }).catch((err) => {
          if (req.destination === 'image') {
            return caches.match('/mr_nobody_logo.jpg').then(res => res || caches.match('/icon-192.svg'));
          }
          throw err;
        });
      });
    })
  );
});
