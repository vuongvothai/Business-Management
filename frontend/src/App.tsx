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
type Customer = { id: number; name: string }
type ExportTicket = {
  id: number
  customerName: string
  itemName: string
  committedVolume: number
  sellingPricePerKg: number
  deliveredVolume: number
  remainingVolume: number
  status: string
  exportDate: string
  deliveries: { id: number; deliveredVolume: number; deliveryDate: string }[]
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
        {activePage === 'imports' && <ImportsPage onCreated={() => setActivePage('overview')} />}
        {activePage === 'price-locks' && <PriceLocksPage />}
        {activePage === 'exports' && <ExportsPage />}
        {activePage !== 'overview' && activePage !== 'imports' && activePage !== 'price-locks' && activePage !== 'exports' && <ModulePlaceholder page={activePage} />}
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

function PriceLocksPage() {
  const [suppliers, setSuppliers] = useState<Supplier[]>([])
  const [items, setItems] = useState<Item[]>([])
  const [supplierId, setSupplierId] = useState('')
  const [itemId, setItemId] = useState('')
  const [lockDate, setLockDate] = useState(new Date().toISOString().slice(0, 10))
  const [volume, setVolume] = useState('')
  const [price, setPrice] = useState('')
  const [notice, setNotice] = useState<string | null>(null)
  const [busy, setBusy] = useState(false)

  const loadData = () => Promise.all([
    fetch('/api/suppliers').then((response) => response.json()),
    fetch('/api/items').then((response) => response.json()),
  ]).then(([supplierData, itemData]) => {
    setSuppliers(supplierData)
    setItems(itemData)
    if (!supplierId && supplierData[0]) setSupplierId(String(supplierData[0].id))
    if (!itemId && itemData[0]) setItemId(String(itemData[0].id))
  })

  useEffect(() => { loadData().catch(() => setNotice('Không thể tải dữ liệu chốt giá.')) }, [])

  const selectedSupplier = suppliers.find((supplier) => String(supplier.id) === supplierId)
  const volumeValue = Number(volume) || 0
  const priceValue = Number(price) || 0

  const createPriceLock = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!supplierId || !itemId || volumeValue <= 0 || priceValue <= 0) {
      setNotice('Vui lòng nhập đầy đủ supplier, mặt hàng, khối lượng và đơn giá hợp lệ.')
      return
    }
    if (selectedSupplier && volumeValue > selectedSupplier.usableCoffeeKg) {
      setNotice(`Khối lượng chốt vượt số dư hiện có (${formatKg(selectedSupplier.usableCoffeeKg)}).`)
      return
    }
    setBusy(true)
    try {
      const response = await fetch('/api/price-locks', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ supplierId: Number(supplierId), itemId: Number(itemId), lockDate, lockedVolume: volumeValue, lockedPricePerKg: priceValue }),
      })
      if (!response.ok) throw new Error()
      const result = await response.json()
      setNotice(`Đã chốt ${formatKg(result.lockedVolume)} · Thành tiền ${formatMoney(result.amount)}.`)
      setVolume('')
      setPrice('')
      await loadData()
    } catch { setNotice('Không thể chốt giá. Số dư có thể đã thay đổi, vui lòng tải lại.') }
    finally { setBusy(false) }
  }

  return <section className="module-page">
    <div className="page-heading"><div><p className="eyebrow">CHỐT GIÁ</p><h2>Chốt giá cà phê</h2><p className="muted">Chốt toàn bộ hoặc một phần số dư của nhà cung cấp.</p></div><button className="secondary-button" type="button" onClick={() => loadData()}>Làm mới</button></div>
    <div className="form-grid">
      <form className="panel form-panel" onSubmit={createPriceLock}>
        <div className="panel-heading"><div><p className="eyebrow">TẠO MỚI</p><h3>Phiếu chốt giá</h3></div></div>
        <div className="field-grid">
          <label>Nhà cung cấp<select value={supplierId} onChange={(event) => setSupplierId(event.target.value)}><option value="">Chọn nhà cung cấp</option>{suppliers.map((supplier) => <option value={supplier.id} key={supplier.id}>{supplier.name} · {formatKg(supplier.usableCoffeeKg)}</option>)}</select></label>
          <label>Mặt hàng<select value={itemId} onChange={(event) => setItemId(event.target.value)}><option value="">Chọn mặt hàng</option>{items.map((item) => <option value={item.id} key={item.id}>{item.name} · {item.category}</option>)}</select></label>
          <label>Ngày chốt<input type="date" value={lockDate} onChange={(event) => setLockDate(event.target.value)} /></label>
          <label>Khối lượng chốt (kg)<input type="number" min="0.001" step="0.001" value={volume} onChange={(event) => setVolume(event.target.value)} placeholder="500.000" /></label>
          <label>Đơn giá (VND/kg)<input type="number" min="0.01" step="1" value={price} onChange={(event) => setPrice(event.target.value)} placeholder="42000" /></label>
        </div>
        <div className="weight-summary"><span>Số dư khả dụng</span><strong>{selectedSupplier ? formatKg(selectedSupplier.usableCoffeeKg) : '—'}</strong></div>
        {notice && <p className="form-notice">{notice}</p>}
        <button className="primary-button" disabled={busy} type="submit">{busy ? 'Đang lưu...' : 'Xác nhận chốt giá'}</button>
      </form>
      <article className="panel"><div className="panel-heading"><div><p className="eyebrow">TÍNH NHANH</p><h3>Giá trị phiếu chốt</h3></div></div><div className="lock-preview"><span>Khối lượng</span><strong>{formatKg(volumeValue)}</strong><span>Đơn giá</span><strong>{formatMoney(priceValue)}</strong><div className="lock-total"><span>Thành tiền dự kiến</span><strong>{formatMoney(volumeValue * priceValue)}</strong></div></div><p className="muted lock-hint">Số dư supplier sẽ được trừ ngay sau khi chốt thành công.</p></article>
    </div>
  </section>
}

function ExportsPage() {
  const [customers, setCustomers] = useState<Customer[]>([])
  const [items, setItems] = useState<Item[]>([])
  const [exports, setExports] = useState<ExportTicket[]>([])
  const [customerId, setCustomerId] = useState('')
  const [itemId, setItemId] = useState('')
  const [exportDate, setExportDate] = useState(new Date().toISOString().slice(0, 10))
  const [committedVolume, setCommittedVolume] = useState('')
  const [sellingPrice, setSellingPrice] = useState('')
  const [notice, setNotice] = useState<string | null>(null)
  const [busy, setBusy] = useState(false)
  const [deliveryVolume, setDeliveryVolume] = useState<Record<number, string>>({})

  const loadData = () => Promise.all([
    fetch('/api/customers').then((response) => response.json()),
    fetch('/api/items').then((response) => response.json()),
    fetch('/api/exports').then((response) => response.json()),
  ]).then(([customerData, itemData, exportData]) => {
    setCustomers(customerData)
    setItems(itemData)
    setExports(exportData)
    if (!customerId && customerData[0]) setCustomerId(String(customerData[0].id))
    if (!itemId && itemData[0]) setItemId(String(itemData[0].id))
  })

  useEffect(() => { loadData().catch(() => setNotice('Không thể tải dữ liệu bán hàng.')) }, [])

  const createExport = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const volume = Number(committedVolume)
    const price = Number(sellingPrice)
    if (!customerId || !itemId || volume <= 0 || price <= 0) {
      setNotice('Vui lòng nhập khách hàng, mặt hàng, khối lượng và giá bán hợp lệ.')
      return
    }
    setBusy(true)
    try {
      const response = await fetch('/api/exports', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ customerId: Number(customerId), itemId: Number(itemId), committedVolume: volume, sellingPricePerKg: price, exportDate }),
      })
      if (!response.ok) throw new Error()
      setCommittedVolume('')
      setSellingPrice('')
      setNotice('Đã tạo phiếu bán. Tồn kho chưa bị trừ.')
      await loadData()
    } catch { setNotice('Không thể tạo phiếu bán.') }
    finally { setBusy(false) }
  }

  const deliver = async (ticket: ExportTicket) => {
    const volume = Number(deliveryVolume[ticket.id])
    if (volume <= 0 || volume > ticket.remainingVolume) {
      setNotice(`Khối lượng giao phải lớn hơn 0 và không vượt ${formatKg(ticket.remainingVolume)}.`)
      return
    }
    setBusy(true)
    try {
      const response = await fetch(`/api/exports/${ticket.id}/deliveries`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ deliveredVolume: volume, deliveryDate: new Date().toISOString().slice(0, 10) }),
      })
      if (!response.ok) throw new Error()
      setDeliveryVolume({ ...deliveryVolume, [ticket.id]: '' })
      setNotice(`Đã ghi nhận giao ${formatKg(volume)}.`)
      await loadData()
    } catch { setNotice('Không thể ghi nhận giao hàng.') }
    finally { setBusy(false) }
  }

  return <section className="module-page">
    <div className="page-heading"><div><p className="eyebrow">BÁN & GIAO HÀNG</p><h2>Phiếu bán và giao hàng</h2><p className="muted">Tạo cam kết bán trước, sau đó ghi nhận từng lần giao thực tế.</p></div><button className="secondary-button" type="button" onClick={() => loadData()}>Làm mới</button></div>
    <form className="panel export-form" onSubmit={createExport}>
      <div className="panel-heading"><div><p className="eyebrow">TẠO MỚI</p><h3>Phiếu chốt bán</h3></div></div>
      <div className="field-grid export-fields">
        <label>Khách hàng<select value={customerId} onChange={(event) => setCustomerId(event.target.value)}><option value="">Chọn khách hàng</option>{customers.map((customer) => <option value={customer.id} key={customer.id}>{customer.name}</option>)}</select></label>
        <label>Mặt hàng<select value={itemId} onChange={(event) => setItemId(event.target.value)}><option value="">Chọn mặt hàng</option>{items.map((item) => <option value={item.id} key={item.id}>{item.name} · {item.category}</option>)}</select></label>
        <label>Ngày bán<input type="date" value={exportDate} onChange={(event) => setExportDate(event.target.value)} /></label>
        <label>Khối lượng cam kết (kg)<input type="number" min="0.001" step="0.001" value={committedVolume} onChange={(event) => setCommittedVolume(event.target.value)} placeholder="1000.000" /></label>
        <label>Giá bán (VND/kg)<input type="number" min="0.01" step="1" value={sellingPrice} onChange={(event) => setSellingPrice(event.target.value)} placeholder="50000" /></label>
      </div>
      {notice && <p className="form-notice">{notice}</p>}
      <button className="primary-button export-submit" disabled={busy} type="submit">{busy ? 'Đang lưu...' : 'Tạo phiếu bán'}</button>
    </form>
    <article className="panel export-list"><div className="panel-heading"><div><p className="eyebrow">THEO DÕI</p><h3>Phiếu bán hiện tại</h3></div><span className="count">{exports.length}</span></div>
      <div className="export-cards">{exports.map((ticket) => <div className="export-card" key={ticket.id}><div className="export-card-head"><div><strong>{ticket.customerName}</strong><span>{ticket.itemName} · {ticket.exportDate}</span></div><span className={`status ${ticket.status.toLowerCase()}`}>{ticket.status}</span></div><div className="export-progress"><div><span>Đã giao</span><strong>{formatKg(ticket.deliveredVolume)}</strong></div><div><span>Còn nợ</span><strong>{formatKg(ticket.remainingVolume)}</strong></div><div><span>Cam kết</span><strong>{formatKg(ticket.committedVolume)}</strong></div></div>{ticket.remainingVolume > 0 && <div className="delivery-entry"><input type="number" min="0.001" max={ticket.remainingVolume} step="0.001" placeholder="Khối lượng giao lần này" value={deliveryVolume[ticket.id] ?? ''} onChange={(event) => setDeliveryVolume({ ...deliveryVolume, [ticket.id]: event.target.value })} /><button className="secondary-button" type="button" disabled={busy} onClick={() => deliver(ticket)}>Ghi nhận giao</button></div>}</div>)}</div>
    </article>
  </section>
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
