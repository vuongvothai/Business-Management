# Feature 5: Dashboard, market prices, and document preview

The frontend now consumes the existing APIs for the date-filtered dashboard (`GET /api/dashboard`), today's market prices (`GET/PUT /api/market-prices/today` and item price routes), and delivery document print data (`GET /api/exports/{exportId}/deliveries/{deliveryId}/print-data`). Users can refresh reports, update manual market prices, and print/save a browser preview as PDF without changing earlier workflows.
