import { useEffect, useState } from 'react'
import { apiFetch } from './api'

type Page = 'dashboard' | 'weigh' | 'purchase' | 'sales' | 'stock' | 'loan' | 'report' | 'master'

const NAV: { key: Page; label: string; icon: string }[] = [
  { key: 'dashboard', label: 'Tổng quan', icon: '▤' },
  { key: 'weigh', label: 'Cân hàng', icon: '⚖' },
  { key: 'purchase', label: 'Nhập hàng', icon: '↓' },
  { key: 'sales', label: 'Xuất hàng', icon: '↑' },
  { key: 'stock', label: 'Kho', icon: '▣' },
  { key: 'loan', label: 'Vay', icon: '₫' },
  { key: 'report', label: 'Báo cáo', icon: '☰' },
  { key: 'master', label: 'Thêm dữ liệu', icon: '✎' },
]

const fmt = (n: number) => new Intl.NumberFormat('vi-VN').format(n)
const fmtMoney = (n: number) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(n)

export default function App() {
  const [page, setPage] = useState<Page>('dashboard')
  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <nav style={{ width: 200, background: '#1f2937', color: '#fff', padding: 12 }}>
        <div style={{ fontWeight: 700, marginBottom: 16 }}>BM</div>
        {NAV.map(n => (
          <button key={n.key} onClick={() => setPage(n.key)}
            style={{ display: 'block', width: '100%', textAlign: 'left', padding: '8px 10px',
              marginBottom: 4, border: 'none', borderRadius: 4,
              background: page === n.key ? '#374151' : 'transparent',
              color: '#fff' }}>
            {n.icon} {n.label}
          </button>
        ))}
      </nav>
      <main style={{ flex: 1, padding: 20 }}>
        {page === 'dashboard' && <Dashboard />}
        {page === 'weigh' && <Weigh />}
        {page === 'purchase' && <Purchase />}
        {page === 'sales' && <Sales />}
        {page === 'stock' && <Stock />}
        {page === 'loan' && <Loan />}
        {page === 'report' && <Report />}
        {page === 'master' && <Master />}
      </main>
    </div>
  )
}

function Card({ title, children }: { title: string; children: React.ReactNode }) {
  return <div style={{ background: '#fff', borderRadius: 8, padding: 16, marginBottom: 16, boxShadow: '0 1px 3px rgba(0,0,0,.1)' }}>
    <h3 style={{ margin: '0 0 12px' }}>{title}</h3>{children}
  </div>
}

function Table({ headers, rows }: { headers: string[]; rows: (string | number)[][] }) {
  return <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 14 }}>
    <thead><tr>{headers.map(h => <th key={h} style={{ textAlign: 'left', borderBottom: '2px solid #e5e7eb', padding: 6 }}>{h}</th>)}</tr></thead>
    <tbody>{rows.map((r, i) => <tr key={i}>{r.map((c, j) => <td key={j} style={{ borderBottom: '1px solid #f3f4f6', padding: 6 }}>{c}</td>)}</tr>)}</tbody>
  </table>
}

function Dashboard() {
  const [month, setMonth] = useState(() => new Date().toISOString().slice(0, 7))
  const [items, setItems] = useState<{ itemId: number; name: string; actualStockKg: number; standardStockKg: number }[]>([])
  useEffect(() => { apiFetch('/api/master/items').then(setItems).catch(() => {}) }, [])
  return <>
    <Card title="Tổng quan kinh doanh">
      <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
        <label>Tháng <input type="month" value={month} onChange={e => setMonth(e.target.value)} /></label>
      </div>
      <div style={{ display: 'flex', gap: 24 }}>
        <Stat label="Doanh thu" value={fmtMoney(0)} />
        <Stat label="Lợi nhuận" value={fmtMoney(0)} />
        <Stat label="Tồn kho" value={`${fmt(items.reduce((s, i) => s + i.actualStockKg, 0))} kg`} />
        <Stat label="Phiếu nhập/xuất" value="0 / 0" />
      </div>
    </Card>
    <Card title="Nhập – xuất theo tấn">
      <p style={{ color: '#6b7280' }}>Biểu đồ sẽ hiển thị khi có dữ liệu.</p>
    </Card>
    <Card title="Hoạt động gần đây">
      <p style={{ color: '#6b7280' }}>Chưa có dữ liệu.</p>
    </Card>
  </>
}

function Stat({ label, value }: { label: string; value: string }) {
  return <div><div style={{ color: '#6b7280', fontSize: 12 }}>{label}</div><div style={{ fontSize: 20, fontWeight: 700 }}>{value}</div></div>
}

function Weigh() {
  const [dir, setDir] = useState('IN')
  const [rows, setRows] = useState<any[]>([])
  useEffect(() => { apiFetch('/api/weigh-tickets').then(setRows).catch(() => {}) }, [])
  return <Card title="Cân hàng">
    <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
      <select value={dir} onChange={e => setDir(e.target.value)}><option value="IN">Cân nhập</option><option value="OUT">Cân xuất</option></select>
      <button onClick={() => window.print()}>In</button>
    </div>
    <Table headers={['Mã', 'Chiều', 'Tổng bao', 'Giờ cân', 'Trạng thái']}
      rows={rows.map(r => [r.code, r.direction, r.totalBags, r.weighedAt, r.status])} />
  </Card>
}

function Purchase() {
  const [rows, setRows] = useState<any[]>([])
  useEffect(() => { apiFetch('/api/purchase/contracts').then(setRows).catch(() => {}) }, [])
  return <Card title="Nhập hàng (Thẳng / Chốt / Từ hàng gửi)">
    <button onClick={() => window.print()}>In</button>
    <Table headers={['Mã HĐ', 'NCC', 'Mặt hàng', 'Cam kết', 'Đã giao', 'Còn lại', 'Trạng thái']}
      rows={rows.map(r => [r.contractCode, r.supplierId, r.itemId, r.committedVolumeKg, r.deliveredVolumeKg, r.remainingVolumeKg, r.status])} />
  </Card>
}

function Sales() {
  const [rows, setRows] = useState<any[]>([])
  useEffect(() => { apiFetch('/api/sales/contracts').then(setRows).catch(() => {}) }, [])
  return <Card title="Xuất hàng (Thẳng / Chốt / Từ hàng gửi)">
    <button onClick={() => window.print()}>In</button>
    <Table headers={['Mã HĐ', 'Khách', 'Loại', 'Cam kết', 'Đã giao', 'Còn lại', 'Trạng thái']}
      rows={rows.map(r => [r.contractCode, r.customerId, r.contractType, r.committedVolumeKg, r.deliveredVolumeKg, r.remainingVolumeKg, r.status])} />
  </Card>
}

function Stock() {
  const [items, setItems] = useState<any[]>([])
  useEffect(() => { apiFetch('/api/master/items').then(setItems).catch(() => {}) }, [])
  return <Card title="Kho">
    <Table headers={['Mặt hàng', 'DVT', 'Tồn thực', 'Tồn quy chuẩn']}
      rows={items.map(i => [i.name, i.unit, i.actualStockKg, i.standardStockKg])} />
  </Card>
}

function Loan() {
  const [rows, setRows] = useState<any[]>([])
  useEffect(() => { apiFetch('/api/loan/contracts').then(setRows).catch(() => {}) }, [])
  return <Card title="Vay (Đi vay / Cho vay)">
    <Table headers={['Mã', 'Loại', 'Đối tượng', 'Gốc', 'Còn lại', 'Trạng thái']}
      rows={rows.map(r => [r.code, r.loanType, r.partyId, r.principalAmount, r.outstandingPrincipal, r.status])} />
  </Card>
}

function Report() {
  return <>
    <Card title="Báo cáo">
      <ul>
        <li>Doanh thu – lợi nhuận</li>
        <li>Nhập – xuất – tồn</li>
        <li>Công nợ</li>
        <li>Cho vay / Đi vay</li>
        <li>Hiệu quả thu mua</li>
      </ul>
    </Card>
    <Card title="Kê khai thuế">
      <button onClick={() => window.print()}>In phiếu kê khai thuế</button>
    </Card>
  </>
}

function Master() {
  const [items, setItems] = useState<any[]>([])
  useEffect(() => { apiFetch('/api/master/items').then(setItems).catch(() => {}) }, [])
  return <Card title="Thêm dữ liệu">
    <Table headers={['ID', 'Tên', 'DVT']} rows={items.map(i => [i.itemId, i.name, i.unit])} />
  </Card>
}