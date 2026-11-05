import { useEffect, useState } from 'react'
import './App.css'

type InventorySummary = {
  totalKg: number
  valuedKg: number
  unpricedKg: number
  totalValue: number
  byCategory: { category: string; totalKg: number; inventoryValue: number }[]
}

type ImportPage = {
  content: {
    id: number
    supplierName: string
    itemName: string
    importDate: string
    totalKg: number
    numberOfBags: number
    status: string
  }[]
  totalElements: number
}

const formatKg = (value: number) =>
  new Intl.NumberFormat('vi-VN', { maximumFractionDigits: 3 }).format(value) + ' kg'

const formatMoney = (value: number) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)

function App() {
  const [summary, setSummary] = useState<InventorySummary | null>(null)
  const [imports, setImports] = useState<ImportPage | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [activePage, setActivePage] = useState('overview')

  useEffect(() => {
    Promise.all([
      fetch('/api/inventory/summary').then((response) => response.json()),
      fetch('/api/imports?page=0&size=5').then((response) => response.json()),
    ])
      .then(([inventory, importPage]) => {
        setSummary(inventory)
        setImports(importPage)
      })
      .catch(() => setError('Không thể kết nối tới backend. Hãy kiểm tra Docker và API.'))
  }, [])

  return (
    <div className="app-shell">
      <header className="topbar">
        <div>
          <div className="brand-mark">BM</div>
          <p className="eyebrow">BUSINESS MANAGEMENT</p>
          <h1>Quản lý cà phê nhân</h1>
        </div>
        <span className="connection"><i /> Hệ thống đang hoạt động</span>
      </header>

      <nav className="main-nav" aria-label="Điều hướng chính">
        <NavButton active={activePage === 'overview'} onClick={() => setActivePage('overview')} icon="⌂">Tổng quan</NavButton>
        <NavButton active={activePage === 'imports'} onClick={() => setActivePage('imports')} icon="↓">Nhập hàng</NavButton>
        <NavButton active={activePage === 'price-locks'} onClick={() => setActivePage('price-locks')} icon="◈">Chốt giá</NavButton>
        <NavButton active={activePage === 'exports'} onClick={() => setActivePage('exports')} icon="↑">Bán & giao hàng</NavButton>
        <NavButton active={activePage === 'master-data'} onClick={() => setActivePage('master-data')} icon="≡">Danh mục</NavButton>
      </nav>

      <main>
        {activePage === 'overview' && <Overview summary={summary} imports={imports} error={error} />}
        {activePage !== 'overview' && <ModulePlaceholder page={activePage} />}
      </main>
      <footer>Business Management · Backend Spring Boot · MySQL</footer>
    </div>
  )
}

function Overview({ summary, imports, error }: { summary: InventorySummary | null; imports: ImportPage | null; error: string | null }) {
  return (
    <>
      <section className="hero">
          <div>
            <p className="eyebrow">TỔNG QUAN KHO</p>
            <h2>Theo dõi tồn kho theo thời gian thực</h2>
            <p className="muted">Số liệu được đồng bộ từ các phiếu nhập, chốt giá và giao hàng.</p>
          </div>
          <div className="hero-mark">KG</div>
        </section>

      {error && <div className="alert">{error}</div>}

      <section className="metrics">
          <Metric label="Tổng tồn kho" value={summary ? formatKg(summary.totalKg) : '—'} accent="green" />
          <Metric label="Đã chốt giá" value={summary ? formatKg(summary.valuedKg) : '—'} accent="amber" />
          <Metric label="Chưa chốt giá" value={summary ? formatKg(summary.unpricedKg) : '—'} accent="blue" />
          <Metric label="Giá trị tồn kho" value={summary ? formatMoney(summary.totalValue) : '—'} accent="dark" />
      </section>

      <section className="content-grid">
          <article className="panel">
            <div className="panel-heading">
              <div><p className="eyebrow">PHÂN BỔ</p><h3>Tồn kho theo loại</h3></div>
            </div>
            <div className="category-list">
              {summary?.byCategory.map((category) => (
                <div className="category-row" key={category.category}>
                  <div className="category-icon">☕</div>
                  <div className="category-info"><strong>{category.category}</strong><span>{formatKg(category.totalKg)}</span></div>
                  <strong>{formatMoney(category.inventoryValue)}</strong>
                </div>
              ))}
              {!summary && <p className="muted">Đang tải dữ liệu...</p>}
            </div>
          </article>

          <article className="panel">
            <div className="panel-heading">
              <div><p className="eyebrow">GIAO DỊCH GẦN ĐÂY</p><h3>Phiếu nhập hàng</h3></div>
              <span className="count">{imports?.totalElements ?? '—'}</span>
            </div>
            <div className="table-wrap">
              <table>
                <thead><tr><th>Ngày</th><th>Nhà cung cấp</th><th>Khối lượng</th></tr></thead>
                <tbody>
                  {imports?.content.map((item) => (
                    <tr key={item.id}><td>{item.importDate}</td><td>{item.supplierName}</td><td><strong>{formatKg(item.totalKg)}</strong></td></tr>
                  ))}
                  {!imports && <tr><td colSpan={3}>Đang tải dữ liệu...</td></tr>}
                </tbody>
              </table>
            </div>
          </article>
      </section>
    </>
  )
}

function ModulePlaceholder({ page }: { page: string }) {
  const pages: Record<string, { eyebrow: string; title: string; description: string }> = {
    imports: { eyebrow: 'NHẬP HÀNG', title: 'Quản lý phiếu nhập', description: 'Tạo phiếu nhập, ghi nhận các lần cân và theo dõi số dư nhà cung cấp.' },
    'price-locks': { eyebrow: 'CHỐT GIÁ', title: 'Quản lý chốt giá', description: 'Theo dõi khối lượng đã chốt, đơn giá và giá trị thanh toán.' },
    exports: { eyebrow: 'BÁN & GIAO HÀNG', title: 'Quản lý phiếu bán', description: 'Theo dõi cam kết bán, các lần giao hàng và khối lượng còn nợ.' },
    'master-data': { eyebrow: 'DANH MỤC', title: 'Dữ liệu nền', description: 'Quản lý nhà cung cấp, khách hàng và mặt hàng.' },
  }
  const content = pages[page]
  return <section className="module-page"><p className="eyebrow">{content.eyebrow}</p><h2>{content.title}</h2><p className="muted">{content.description}</p><div className="module-card"><span className="module-icon">✦</span><div><h3>Màn hình đang được hoàn thiện</h3><p className="muted">API backend cho nghiệp vụ này đã sẵn sàng. Bước tiếp theo là kết nối biểu mẫu và bảng dữ liệu.</p></div></div></section>
}

function NavButton({ active, onClick, icon, children }: { active: boolean; onClick: () => void; icon: string; children: string }) {
  return <button type="button" className={`nav-button${active ? ' active' : ''}`} onClick={onClick}><span>{icon}</span>{children}</button>
}

function Metric({ label, value, accent }: { label: string; value: string; accent: string }) {
  return <div className={`metric ${accent}`}><span className="metric-dot" /><p>{label}</p><strong>{value}</strong></div>
}

export default App
