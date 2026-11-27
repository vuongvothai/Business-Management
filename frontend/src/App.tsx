import { useEffect, useState } from 'react'
import { apiFetch } from './api'
import WeighPage from './WeighPage'
import './App.css'

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

export default function App() {
  const [page, setPage] = useState<Page>('dashboard')
  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="brand">Hệ thống &gt; {NAV.find(n => n.key === page)?.label}</div>
        <div className="user-menu">AD</div>
      </header>
      <nav className="app-nav">
        <div className="nav-header">MENU CHÍNH</div>
        {NAV.map(n => (
          <button key={n.key} onClick={() => setPage(n.key)}
            className={`nav-button ${page === n.key ? 'active' : ''}`}>
            <span className="nav-icon">{n.icon}</span> {n.label}
          </button>
        ))}
      </nav>
      <main className="app-main">
        {page === 'dashboard' && <Dashboard />}
                {page === 'weigh' && <WeighPage />}
                {page !== 'dashboard' && page !== 'weigh' && <Placeholder title={NAV.find(n => n.key === page)?.label ?? ''} />}
      </main>
    </div>
  )
}

function Dashboard() {
  const [month, setMonth] = useState(() => new Date().toISOString().slice(0, 7))
  return <>
    <div className="page-header">
      <h2>Tổng quan kinh doanh</h2>
      <div className="page-actions">
        <button className="filter-btn active">Hệ thống hoạt động</button>
        <select value={month} onChange={e => setMonth(e.target.value)}>
          <option value="2026-11">Tháng này</option>
          <option value="2026-10">Tháng trước</option>
        </select>
      </div>
    </div>
    <div className="stats-grid">
      <StatCard label="Doanh thu" value="8,65 tỷ" trend="+12,5%" />
      <StatCard label="Lợi nhuận tạm tính" value="425 triệu" />
      <StatCard label="Tổng tồn kho chuẩn" value="156,8 tấn" />
      <StatCard label="Phiếu cân hôm nay" value="32" />
    </div>
    <div className="card">
      <div className="card-header">
        <h3 className="card-title">Tổng quan nhập – xuất</h3>
        <select defaultValue="Tháng này"><option>Tháng này</option></select>
      </div>
      <ProgressBar label="Nhập hàng" value={85.6} max={100} unit="tấn" color="var(--color-primary-light)" />
      <ProgressBar label="Xuất hàng" value={62.4} max={100} unit="tấn" color="#3b82f6" />
    </div>
    <div className="card">
      <div className="card-header">
        <h3 className="card-title">Hoạt động gần đây</h3>
        <select defaultValue="Tất cả"><option>Tất cả</option></select>
      </div>
      <ActivityList />
    </div>
  </>
}

function StatCard({ label, value, trend }: { label: string; value: string; trend?: string }) {
  return <div className="stat-card">
    <div className="stat-label">{label}</div>
    <div className="stat-value">{value}</div>
    {trend && <div className="stat-trend">{trend}</div>}
  </div>
}

function ProgressBar({ label, value, max, unit, color }: { label: string; value: number; max: number; unit: string; color: string }) {
  return <div className="progress-bar-container">
    <div className="progress-label">{label}</div>
    <div className="progress-track">
      <div className="progress-fill" style={{ width: `${(value / max) * 100}%`, backgroundColor: color }}></div>
    </div>
    <div className="progress-value">{value} {unit}</div>
  </div>
}

function ActivityList() {
  const activities = [
    { type: 'Nhập hàng', code: 'NH-0128', desc: 'Nguyễn Văn Hùng · 12.500 kg', status: 'Hoàn thành', color: '#22c55e' },
    { type: 'Xuất hàng', code: 'XH-0086', desc: 'Khách hàng Minh Phát · 8.000 kg', status: 'Đang giao', color: '#3b82f6' },
    { type: 'Phiếu cân', code: 'CN-0238', desc: 'Bà 87C · 125.41 · 16.200 kg', status: 'Đã cân', color: '#f97316' },
    { type: 'Hợp đồng vay', code: 'VKF-0012', desc: 'Vay vốn · 500 triệu', status: 'Đang hiệu lực', color: '#ef4444' },
  ]
  return <div className="activity-list">
    {activities.map((a, i) =>
      <div key={i} className="activity-item">
        <div className="activity-icon" style={{ color: a.color }}>●</div>
        <div className="activity-details">
          <div className="activity-title">{a.type} {a.code}</div>
          <div className="activity-desc">{a.desc}</div>
        </div>
        <div className="activity-status" style={{ color: a.color }}>{a.status}</div>
      </div>
    )}
  </div>
}

function Placeholder({ title }: { title: string }) {
  return <div className="card">
    <h2 className="card-title">{title}</h2>
    <p>Giao diện đang được xây dựng.</p>
  </div>
}