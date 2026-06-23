# Paano I-publish ang AnimeHub (Libre, Step-by-Step)

Dalawang parte ito: (1) ilagay ang code sa GitHub, (2) i-deploy mula sa GitHub papunta sa Render — isang libreng hosting na kayang patakbuhin ang Spring Boot apps. Gagawin mo ang mga steps na ito sa **sariling computer mo** (hindi sa sandbox ni Claude), dahil kailangan ng access sa GitHub account mo.

---

## Bahagi 1: I-push sa GitHub

1. Kung wala ka pang GitHub account, gumawa muna sa **github.com** (libre).
2. Sa GitHub, gawa ng **bagong repository** (i-click ang "New repository"). Pangalanan mo, hal. `animehub`. Iwanan walang README/license (meron na tayo).
3. I-unzip ang `animehub.zip` na ibinigay ko, buksan ang terminal sa loob ng folder na `animehub`, tapos i-run isa-isa:

```bash
git init
git add .
git commit -m "Initial commit - AnimeHub Spring Boot project"
git branch -M main
git remote add origin https://github.com/USERNAME-MO/animehub.git
git push -u origin main
```

> Palitan ang `USERNAME-MO/animehub` ng totoong GitHub username at repo name mo. Hihingin sa'yo ng GitHub ang login (o personal access token) sa unang push.

4. I-refresh ang GitHub page mo — dapat makita mo na ang lahat ng files.

---

## Bahagi 2: I-deploy sa Render (libre)

1. Pumunta sa **render.com** → Sign up gamit ang GitHub account mo (mas mabilis, awtomatikong naka-connect).
2. Sa Render dashboard, i-click **"New +"** → **"Web Service"**.
3. Piliin ang `animehub` repo mo mula sa listahan.
4. Render dapat awtomatikong makita ang `Dockerfile` natin at i-set ang Environment sa **Docker**. Kung hindi, manually piliin: **Environment: Docker**.
5. Settings:
   - **Name:** `animehub` (o kahit ano)
   - **Region:** pumili ng pinakamalapit
   - **Instance Type:** **Free**
6. I-click **"Create Web Service"**.
7. Hihintayin mong matapos ang build (3-5 minuto). Makikita mo ang logs habang nag-build.
8. Pagkatapos, makikita mo sa taas ng page ang live URL mo, hal:
   `https://animehub-xxxx.onrender.com`

Ito na ang link na maipapadala mo sa professor mo!

---

## Mahalagang paalala

- **Free tier ni Render** ay "natutulog" (sleeps) ang app kapag walang traffic ng ~15 minuto. Magising ito ulit (~30-50 segundo) sa unang request. Normal lang ito sa free hosting — sabihin lang sa professor mo na "mag-load lang ng konti sa unang open."
- **H2 database** na ginamit natin ay **in-memory** — mawawala ang naka-register na users/watchlist data tuwing mag-restart/mag-sleep ang app. Sapat na ito para sa demo, pero kung kailangan mong permanenteng data, sabihin mo lang at gagawa tayo ng libreng PostgreSQL database sa Render (madali lang idagdag).
- Kung may build error sa Render logs, i-copy-paste mo lang sa akin ang error message at tutulungan kitang ayusin.

---

## Mabilisang checklist

- [ ] Na-push sa GitHub
- [ ] Gumawa ng Render account
- [ ] Na-connect ang repo sa Render
- [ ] Successful ang build
- [ ] May live URL na

