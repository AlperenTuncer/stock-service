# Stock Service

**Stock Service**, mikroservis tabanlı bir e-ticaret sisteminde **stok yönetimi ve rezervasyon** işlevlerini sağlar.  
Java 21 ve Spring Boot 3.5.4 kullanılarak geliştirilmiştir.

Servis, **SOLID prensiplerine** uygun katmanlı mimariyle tasarlanmıştır ve ileride **Saga Pattern (Choreography)** yapısı altında Order Service ve Payment Service ile entegre edilmek üzere hazırlanmıştır.

---

## 🚀 Özellikler

- Sipariş bazlı stok rezervasyonu yapma
- Rezervasyonu onaylama (commit) → ürün miktarını düşme
- Rezervasyonu serbest bırakma (release)
- Yetersiz stok durumunda **REJECTED** dönüşü
- H2 (in-memory) veritabanı desteği (geliştirme için)
- PostgreSQL uyumluluğu (prod ortamına geçiş için)
- Bean Validation ile DTO seviyesinde doğrulama
- Global Exception Handler ile tutarlı hata yanıtları
- Katmanlı mimari:
  - **Domain** (iş kuralları, invariants)
  - **Repository** (arayüz)
  - **Persistence** (JPA adaptörleri, mapping)
  - **Service** (use-case mantığı)
  - **API/Controller** (REST endpoint’leri)

---

## 🛠 Kullanılan Teknolojiler

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Jakarta Bean Validation
- JUnit 5, Mockito, AssertJ

---

## 📂 Proje Yapısı

```text
src/main/java/com/alperen/stock
 ├── api/             # DTO'lar, mapper, exception handler
 ├── controller/      # REST endpoint’leri
 ├── domain/          # Domain modelleri (Product, Reservation, vb.)
 ├── repository/      # Repository arayüzleri
 ├── persistence/     # JPA entity, mapper, adapter
 ├── service/         # İş mantığı (StockService interface & impl)
 └── StockServiceApplication.java
```

---

## ⚙️ Çalıştırma

### 1. Bağımlılıkları indir

```bash
mvn clean install
```

### 2. Geliştirme profili ile çalıştır

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

> `application-dev.yml` içinde H2 veritabanı ve `server.port: 8082` ayarları bulunur.

### 3. Prod (PostgreSQL) için

`application-prod.yml` içinde PostgreSQL ayarlarını yap ve:

```bash
java -jar target/stock-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 📡 API Endpoint’leri

### 1. Stok Rezervasyonu

**POST** `/api/v1/stock/reservations`

```json
{
  "orderId": "11111111-1111-1111-1111-111111111111",
  "items": [
    {
      "productId": "22222222-2222-2222-2222-222222222222",
      "quantity": 2
    }
  ]
}
```

Yanıt (200 OK):

```json
{
  "id": "33333333-3333-3333-3333-333333333333",
  "orderId": "11111111-1111-1111-1111-111111111111",
  "status": "RESERVED",
  "createdAt": "2025-08-29T16:20:31.111Z"
}
```

### 2. Rezervasyonu Onayla (Commit)

**POST** `/api/v1/stock/reservations/{orderId}/commit`

Yanıt:

```json
{ "orderId": "11111111-1111-1111-1111-111111111111", "status": "COMMITTED" }
```

### 3. Rezervasyonu Serbest Bırak (Release)

**POST** `/api/v1/stock/reservations/{orderId}/release`

Yanıt:

```json
{ "orderId": "11111111-1111-1111-1111-111111111111", "status": "RELEASED" }
```

---

## 🧪 Testler

Testler Maven üzerinden çalıştırılır:

```bash
mvn test
```

Test katmanları:

- **Domain Unit Test**: Reservation, Product kuralları
- **Service Unit Test**: `StockServiceImpl` (Mockito ile mock repo)
- **Repository Slice Test**: JPA mapping & H2 testleri
- **Controller Test**: @WebMvcTest ile API + Validasyon

---

## 📈 Geliştirme Planı

- [x] Order Service (H2 + CRUD)
- [x] Stock Service (REST tabanlı prototip)
- [ ] Payment Service (REST tabanlı prototip)
- [ ] Saga Pattern (RabbitMQ üzerinden event-driven)
- [ ] Redis (idempotency, cache, kısa süreli state)
- [ ] Spring Cloud (Config Server, Gateway, Circuit Breaker)

---

## 👤 Yazar

**Alperen Tuncer**  
Backend Developer

---

## 📜 Lisans

MIT License. Bu proje özgürce kullanılabilir ve geliştirilebilir.
