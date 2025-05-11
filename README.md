# 📡 Real-Time Air Quality Platform

Bu proje, hava kalitesi verilerini gerçek zamanlı olarak izlemek, analiz etmek ve anomalileri tespit etmek üzere geliştirilmiş mikroservis tabanlı bir sistemdir. Gelişmiş görselleştirme ve senaryo test altyapısıyla tam entegre bir çözümdür.

---

## 📋 İçindekiler

- [🎯 Projenin Amacı ve Kapsamı](#-projenin-amacı-ve-kapsamı)
- [🏗 Sistem Mimarisi ve Komponentler](#-sistem-mimarisi-ve-komponentler)
- [🧩 Teknoloji Seçimleri](#-teknoloji-seçimleri)
- [🚀 Kurulum Adımları](#-kurulum-adımları)
- [🧑‍💻 Kullanım Rehberi](#-kullanım-rehberi)
- [🛠 API Dokümantasyonu](#-api-dokümantasyonu)
- [📜 Script Kullanımı](#-script-kullanımı)
- [🩺 Troubleshooting](#-troubleshooting)

---

## 🎯 Projenin Amacı ve Kapsamı

- Gerçek zamanlı hava kalitesi verilerini toplar ve işler.
- Anomalileri otomatik tespit eder.
- Kullanıcılara görsel analiz ve canlı bildirim sunar.
- Manuel ve otomatik test senaryoları sağlar.

---

## 🏗 Sistem Mimarisi ve Komponentler

### 🔌 Backend Mikroservisleri

| Servis                        | Açıklama                                                                                             | Port |
| ----------------------------- | ---------------------------------------------------------------------------------------------------- | ---- |
| **air-data-service**          | OpenWeatherMap'ten veri çeker veya manuel veri alır ve Kafka'ya yazar. Ayrıca AQI hesaplaması yapar. | 8081 |
| **anomaly-detection-service** | Kafka'dan gelen verilerle anomali tespiti yapar ve detaylı açıklamalarla veritabanına kaydeder.      | 8082 |
| **data-storage-service**      | Kafka'dan gelen verileri PostgreSQL'e kaydeder, ayrıca bölgesel analiz için sorgular sağlar.         | 8083 |
| **notification-service**      | anomaly-detection-service'den gelen son anomaly'yi Server-Sent Events (SSE) ile frontend'e iletir.   | 8084 |
| **script-runner-service**     | shell scriptlerini çalıştırarak test verisi gönderimini mümkün kılar (manual ve otomatik testler).   | 8085 |

### 🌐 Frontend

- React + Ant Design tabanlı UI.
- Gerçek zamanlı anomaly bildirimi, harita, grafik, form sayfaları içerir.
- Port: `3000`

### 📦 Ortak Bağımlılıklar

- **Kafka + Zookeeper**: Tüm mikroservisler arası veri akışını sağlar.
- **PostgreSQL**: Hava kalitesi ve anomaly verilerinin kalıcı olarak saklandığı veritabanıdır.

---

## 🧩 Teknoloji Seçimleri

- Spring Boot (Java 17+)
- Kafka / Zookeeper
- PostgreSQL 15
- React + Ant Design
- Docker & Docker Compose
- leaflet.js, recharts, SSE

---

## 🚀 Kurulum Adımları (Detaylı)

### 1. Bağımlılıkları Yükleyin

```bash
git clone https://github.com/gkhancobanoglu/real-time-air-quality-platform.git
cd real-time-air-quality-platform
```

> Gerekirse `scripts/*.sh` dosyalarına çalıştırma izni verin:

```bash
chmod +x backend/scripts/*.sh
```

### 2. Docker Compose ile Tüm Servisleri Başlatın

```bash
docker-compose up --build
```

> İlk çalıştırma 2-5 dakika sürebilir.

### 3. Frontend'e Erişim

http://localhost:3000

### 4. Servislerin Portları

| Servis                | URL                   |
| --------------------- | --------------------- |
| Frontend              | http://localhost:3000 |
| air-data-service      | http://localhost:8081 |
| anomaly-detection     | http://localhost:8082 |
| data-storage-service  | http://localhost:8083 |
| notification-service  | http://localhost:8084 |
| script-runner-service | http://localhost:8085 |

---

## 🧑‍💻 Kullanım Rehberi

- **Dashboard:** Harita, grafik, anomaly listesi ve test sayfalarına yönlendirir.
- **Manual Input:** Kirletici, lokasyon ve değer seçerek elle veri gönderimi sağlar.
- **Auto Test:** Belirli süre boyunca rastgele veri göndererek sistemin dayanıklılığını test eder.
- **Anomaly List / Detail:** Anomalileri listeler ve detaylarını sunar.
- **Regional Analysis:** Belirli bir alan için ortalama kirlilik oranlarını hesaplar.
- **Heatmap:** Anomalileri dünya çapında renk yoğunluğu ile görselleştirir.

---

## 🛠 API Dokümantasyonu

### air-data-service

- `GET /api/air?lat={lat}&lon={lon}` – OpenWeatherMap'ten veri alır, Kafka'ya gönderir.
- `POST /api/air/data` – JSON body ile manuel veri alır, Kafka'ya gönderir ve AQI hesaplar.

### anomaly-detection-service

- `GET /api/anomalies` – Tüm anomaly kayıtlarını döner.
- `GET /api/anomalies/latest` – Son anomaly kaydını döner.
- `GET /api/anomalies/{id}` – ID'ye özel anomaly verisi getirir.

### data-storage-service

- `GET /api/air-quality` – Tüm kayıtlı hava kalitesi verileri.
- `GET /api/air-quality/pollution/region?minLat=x&maxLat=y&minLon=a&maxLon=b` – Bölgesel ortalamaları döner.

### notification-service

- `GET /api/notifications/stream` – SSE ile anomaly bildirimlerini yayınlar.

### script-runner-service

- `POST /api/scripts/manual?lat=&lon=&param=&value=` – manual-input.sh script'ini çalıştırır.
- `POST /api/scripts/autotest?duration=&rate=&anomalyChance=` – auto-test.sh script'ini çalıştırır.

---

## 📜 Script Kullanımı

### `manual-input.sh`

```bash
./manual-input.sh <lat> <lon> <co> <no> <no2> <o3> <so2> <pm2_5> <pm10> <nh3>
```

Manuel olarak veri gönderir.

### `auto-test.sh`

```bash
./auto-test.sh <duration> <rate> <anomalyChance>
```

Verilen süre boyunca belirli hızda rastgele veri üretip gönderir.

---

## 🩺 Troubleshooting

| Sorun                      | Çözüm                                                    |
| -------------------------- | -------------------------------------------------------- |
| PostgreSQL bağlantı hatası | `init.sql` doğru mount edildi mi? Port 5432 açık mı?     |
| Kafka bağlantı hatası      | `localhost` yerine `kafka` host adı kullanıldı mı?       |
| Veriler görünmüyor         | `docker-compose logs -f` ile ilgili servisi kontrol edin |
| SSE çalışmıyor             | Tarayıcı CORS/SSE loglarını kontrol edin                 |

---

📦 Proje kurulum ve çalıştırmaya hazır. Anomali tespiti, görselleştirme ve test altyapısıyla üretime yönelik geliştirmeler için güçlü bir temel sunar.
