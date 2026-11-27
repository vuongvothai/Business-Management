import { useEffect, useState } from 'react'
import { apiFetch } from './api'

type WeighTicket = {
  weighTicketId: number
  code: string
  direction: string
  weighedAt: string
  warehouseId?: number
  vehicleId?: number
  truckLicensePlate?: string
  totalBags: number
  grossWeightKg: number
  tareWeightKg: number
  netWeightKg: number
  moisturePct?: number
  impurityPct?: number
  moistureStd: number
  impurityStd: number
  standardWeightKg: number
  sampleTestResult?: string
  note?: string
  status: string
}

type Warehouse = { warehouseId: number; name: string }
type Vehicle = { vehicleId: number; licensePlate: string }

export default function WeighPage() {
  const [tickets, setTickets] = useState<WeighTicket[]>([])
  const [warehouses, setWarehouses] = useState<Warehouse[]>([])
  const [vehicles, setVehicles] = useState<Vehicle[]>([])
  const [filter, setFilter] = useState('all')

  const [warehouseId, setWarehouseId] = useState('')
  const [vehicleId, setVehicleId] = useState('')
  const [gross, setGross] = useState('')
  const [tare, setTare] = useState('')
  const [bags, setBags] = useState(1)
  const [moisture, setMoisture] = useState('')
  const [impurity, setImpurity] = useState('')
  const [note, setNote] = useState('')
  const [msg, setMsg] = useState('')

  const load = () => apiFetch<WeighTicket[]>('/api/weigh-tickets').then(setTickets).catch(() => {})
  useEffect(() => {
    load()
    apiFetch<Warehouse[]>('/api/master/warehouses').then(setWarehouses).catch(() => {})
    apiFetch<Vehicle[]>('/api/master/vehicles').then(setVehicles).catch(() => {})
  }, [])

  const net = (parseFloat(gross) || 0) - (parseFloat(tare) || 0)
  const filtered = filter === 'all' ? tickets : tickets.filter(t => t.direction === filter)

  const submit = async () => {
    try {
      await apiFetch('/api/weigh-tickets', {
        method: 'POST',
        body: JSON.stringify({
          direction: filter === 'OUT' ? 'OUT' : 'IN',
          weighedAt: new Date().toISOString(),
          warehouseId: warehouseId ? parseInt(warehouseId) : null,
          vehicleId: vehicleId ? parseInt(vehicleId) : null,
          truckLicensePlate: vehicles.find(v => v.vehicleId === parseInt(vehicleId))?.licensePlate ?? null,
          createdBy: 1,
          totalBags: bags,
          moisturePct: parseFloat(moisture) || null,
          impurityPct: parseFloat(impurity) || null,
          note: note || null,
          lines: [{ lineNo: 1, weightKg: parseFloat(gross) || 0, bagCount: bags, tarePerBagKg: parseFloat(tare) || 0 }],
        }),
      })
      setMsg('Đã tạo phiếu cân')
      setGross(''); setTare(''); setMoisture(''); setImpurity(''); setNote('')
      load()
    } catch (e: any) { setMsg(e.message) }
  }

  return <>
    <div className="page-header">
      <h2>Cân hàng</h2>
      <div className="page-actions">
        <select value={filter} onChange={e => setFilter(e.target.value)}>
          <option value="all">Tất cả</option>
          <option value="IN">Cân nhập</option>
          <option value="OUT">Cân xuất</option>
        </select>
        <button className="filter-btn" onClick={() => window.print()}>In</button>
      </div>
    </div>

    <div className="stats-grid">
      <StatCard label="Tổng phiếu" value={String(tickets.length)} />
      <StatCard label="Đã nhập" value={String(tickets.filter(t => t.direction === 'IN').length)} />
      <StatCard label="Đã xuất" value={String(tickets.filter(t => t.direction === 'OUT').length)} />
      <StatCard label="Tổng kg thực" value={tickets.reduce((s, t) => s + t.netWeightKg, 0).toLocaleString('vi-VN')} />
    </div>

    <div className="card">
      <div className="card-header">
        <h3 className="card-title">Tạo phiếu cân</h3>
      </div>

      <div className="form-row-2">
        <div className="form-group">
          <label className="form-label">Kho</label>
          <select className="form-input" value={warehouseId} onChange={e => setWarehouseId(e.target.value)}>
            <option value="">-- Chọn kho --</option>
            {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Số xe</label>
          <select className="form-input" value={vehicleId} onChange={e => setVehicleId(e.target.value)}>
            <option value="">-- Chọn xe --</option>
            {vehicles.map(v => <option key={v.vehicleId} value={v.vehicleId}>{v.licensePlate}</option>)}
          </select>
        </div>
      </div>

      <div className="form-row-2">
        <div className="form-group">
          <label className="form-label">Số bao</label>
          <input className="form-input" type="number" value={bags} onChange={e => setBags(parseInt(e.target.value) || 1)} />
        </div>
        <div className="form-group">
          <label className="form-label">Trọng lượng tổng (kg)</label>
          <input className="form-input" type="number" value={gross} onChange={e => setGross(e.target.value)} />
        </div>
      </div>

      <div className="form-row-2">
        <div className="form-group">
          <label className="form-label">Trọng lượng xe (kg)</label>
          <input className="form-input" type="number" value={tare} onChange={e => setTare(e.target.value)} />
        </div>
        <div className="form-group">
          <label className="form-label">Độ ẩm (%)</label>
          <input className="form-input" type="number" step="0.1" value={moisture} onChange={e => setMoisture(e.target.value)} />
        </div>
      </div>

      <div className="form-row-2">
        <div className="form-group">
          <label className="form-label">Tạp chất (%)</label>
          <input className="form-input" type="number" step="0.1" value={impurity} onChange={e => setImpurity(e.target.value)} />
        </div>
        <div className="form-group">
          <label className="form-label">Ghi chú</label>
          <input className="form-input" value={note} onChange={e => setNote(e.target.value)} />
        </div>
      </div>

      <div className="net-weight-box">
        <div className="net-weight-label">Trọng lượng hàng sau trừ bì</div>
        <div className="net-weight-value">{net.toLocaleString('vi-VN')} kg</div>
      </div>

      {msg && <p style={{ color: msg.includes('Đã') ? '#16a34a' : '#dc2626', fontSize: 13 }}>{msg}</p>}
      <button className="btn-submit" onClick={submit}>Lưu phiếu cân</button>
    </div>

    <div className="card">
      <div className="card-header">
        <h3 className="card-title">Danh sách phiếu cân</h3>
      </div>
      <table>
        <thead>
          <tr>
            <th>Mã</th><th>Chiều</th><th>Kho</th><th>Xe</th><th>Bao</th>
            <th>Tổng kg</th><th>Trừ bì</th><th>Thực</th><th>Ẩm</th><th>Tạp</th><th>Quy chuẩn</th><th>Trạng thái</th>
          </tr>
        </thead>
        <tbody>
          {filtered.map(t => (
            <tr key={t.weighTicketId}>
              <td>{t.code}</td>
              <td>{t.direction === 'IN' ? 'Nhập' : 'Xuất'}</td>
              <td>{warehouses.find(w => w.warehouseId === t.warehouseId)?.name ?? '-'}</td>
              <td>{t.truckLicensePlate ?? '-'}</td>
              <td>{t.totalBags}</td>
              <td>{t.grossWeightKg.toLocaleString('vi-VN')}</td>
              <td>{t.tareWeightKg.toLocaleString('vi-VN')}</td>
              <td>{t.netWeightKg.toLocaleString('vi-VN')}</td>
              <td>{t.moisturePct?.toLocaleString('vi-VN') ?? '-'}</td>
              <td>{t.impurityPct?.toLocaleString('vi-VN') ?? '-'}</td>
              <td>{t.standardWeightKg.toLocaleString('vi-VN')}</td>
              <td><span className="status-badge">{t.status}</span></td>
            </tr>
          ))}
          {filtered.length === 0 && <tr><td colSpan={12} style={{ textAlign: 'center', color: 'var(--color-text-muted)' }}>Chưa có dữ liệu</td></tr>}
        </tbody>
      </table>
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