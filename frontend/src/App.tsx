import { useEffect, useState, type FormEvent } from 'react'
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

type Supplier = { id: number; name: string; usableCoffeeKg: number }
type Item = { id: number; name: string; category: string; unit: string }

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
        {activePage === 'imports' && <ImportsPage onCreated={() => setActivePage('overview')} />}
        {activePage !== 'overview' && activePage !== 'imports' && <ModulePlaceholder page={activePage} />}
      </main>
      <footer>Business Management · Backend Spring Boot · MySQL</footer>
    </div>
  )
}

function ImportsPage({ onCreated }: { onCreated: () => void }) {
  const [suppliers, setSuppliers] = useState<Supplier[]>([])
  const [items, setItems] = useState<Item[]>([])
  const [imports, setImports] = useState<ImportPage | null>(null)
  const [supplierId, setSupplierId] = useState('')
  const [itemId, setItemId] = useState('')
  const [importDate, setImportDate] = useState(new Date().toISOString().slice(0, 10))
  const [weightKg, setWeightKg] = useState('')
  const [bagCount, setBagCount] = useState('1')
  const [weights, setWeights] = useState<{ weightKg: number; bagCount: number }[]>([])
  const [notice, setNotice] = useState<string | null>(null)
  const [busy, setBusy] = useState(false)

  const loadData = () => Promise.all([
    fetch('/api/suppliers').then((response) => response.json()),
    fetch('/api/items').then((response) => response.json()),
    fetch('/api/imports?page=0&size=20').then((response) => response.json()),
  ]).then(([supplierData, itemData, importData]) => {
    setSuppliers(supplierData)
    setItems(itemData)
    setImports(importData)
    if (!supplierId && supplierData[0]) setSupplierId(String(supplierData[0].id))
    if (!itemId && itemData[0]) setItemId(String(itemData[0].id))
  })

  useEffect(() => { loadData().catch(() => setNotice('Không thể tải dữ liệu nhập hàng.')) }, [])

  const addWeight = () => {
    const weight = Number(weightKg)
    const bags = Number(bagCount)
    if (!weight || weight <= 0 || ![1, 2].includes(bags)) {
      setNotice('Khối lượng phải lớn hơn 0 và số bao mỗi lần cân là 1 hoặc 2.')
      return
    }
    setWeights([...weights, { weightKg: weight, bagCount: bags }])
    setWeightKg('')
    setNotice(null)
  }

  const createImport = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!supplierId || !itemId || weights.length === 0) {
      setNotice('Vui lòng chọn nhà cung cấp, mặt hàng và thêm ít nhất một lần cân.')
      return
    }
    setBusy(true)
    try {
      const response = await fetch('/api/imports', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ supplierId: Number(supplierId), itemId: Number(itemId), importDate, weights }),
      })
      if (!response.ok) throw new Error()
      setWeights([])
      setNotice('Đã tạo phiếu nhập thành công.')
      await loadData()
      onCreated()
    } catch { setNotice('Không thể tạo phiếu nhập. Vui lòng kiểm tra dữ liệu.') }
    finally { setBusy(false) }
  }

  const totalKg = weights.reduce((sum, weight) => sum + weight.weightKg, 0)
  const totalBags = weights.reduce((sum, weight) => sum + weight.bagCount, 0)
  return <section className="module-page">
    <div className="page-heading"><div><p className="eyebrow">NHẬP HÀNG</p><h2>Quản lý phiếu nhập</h2><p className="muted">Ghi nhận từng lần cân và cập nhật tồn kho tự động.</p></div><button className="secondary-button" type="button" onClick={() => loadData()}>Làm mới</button></div>
    <div className="form-grid">
      <form className="panel form-panel" onSubmit={createImport}>
        <div className="panel-heading"><div><p className="eyebrow">TẠO MỚI</p><h3>Phiếu nhập kho</h3></div></div>
        <div className="field-grid">
          <label>Nhà cung cấp<select value={supplierId} onChange={(event) => setSupplierId(event.target.value)}><option value="">Chọn nhà cung cấp</option>{suppliers.map((supplier) => <option value={supplier.id} key={supplier.id}>{supplier.name}</option>)}</select></label>
          <label>Mặt hàng<select value={itemId} onChange={(event) => setItemId(event.target.value)}><option value="">Chọn mặt hàng</option>{items.map((item) => <option value={item.id} key={item.id}>{item.name} · {item.category}</option>)}</select></label>
          <label>Ngày nhập<input type="date" value={importDate} onChange={(event) => setImportDate(event.target.value)} /></label>
        </div>
        <div className="weight-entry"><label>Khối lượng (kg)<input type="number" min="0.001" step="0.001" value={weightKg} onChange={(event) => setWeightKg(event.target.value)} placeholder="120.300" /></label><label>Số bao<input type="number" min="1" max="2" value={bagCount} onChange={(event) => setBagCount(event.target.value)} /></label><button className="secondary-button add-button" type="button" onClick={addWeight}>+ Thêm lần cân</button></div>
        <div className="weight-summary"><span>{weights.length} lần cân</span><strong>{totalBags} bao · {formatKg(totalKg)}</strong></div>
        {weights.length > 0 && <div className="weight-list">{weights.map((weight, index) => <div key={`${weight.weightKg}-${index}`}><span>Lần cân {index + 1}</span><strong>{weight.bagCount} bao · {formatKg(weight.weightKg)}</strong><button type="button" aria-label="Xóa lần cân" onClick={() => setWeights(weights.filter((_, current) => current !== index))}>×</button></div>)}</div>}
        {notice && <p className="form-notice">{notice}</p>}
        <button className="primary-button" disabled={busy} type="submit">{busy ? 'Đang lưu...' : 'Tạo phiếu nhập'}</button>
      </form>
      <article className="panel"><div className="panel-heading"><div><p className="eyebrow">SỔ NHẬP</p><h3>Phiếu nhập gần đây</h3></div><span className="count">{imports?.totalElements ?? '—'}</span></div><div className="table-wrap"><table><thead><tr><th>Ngày</th><th>Nhà cung cấp</th><th>Số bao</th><th>Khối lượng</th></tr></thead><tbody>{imports?.content.map((item) => <tr key={item.id}><td>{item.importDate}</td><td>{item.supplierName}</td><td>{item.numberOfBags}</td><td><strong>{formatKg(item.totalKg)}</strong></td></tr>)}</tbody></table></div></article>
    </div>
  </section>
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
