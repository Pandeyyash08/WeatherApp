const apiKey = "28f61f4b78b8f9cc89e00e40149d3448";

const cityInput = document.getElementById("cityInput");
const searchBtn = document.getElementById("searchBtn");
const weatherDisplay = document.getElementById("weatherDisplay");
const forecastContainer = document.getElementById("forecastContainer");
const forecastCards = document.getElementById("forecastCards");
const cityNameEl = document.getElementById("cityName");
const temperatureEl = document.getElementById("temperature");
const conditionEl = document.getElementById("condition");
const humidityEl = document.getElementById("humidity");
const windEl = document.getElementById("wind");
const addFavoriteBtn = document.getElementById("addFavorite");
const favoriteList = document.getElementById("favoriteList");

let currentCity = "";

// 🔍 Search city
searchBtn.addEventListener("click", () => {
  const city = cityInput.value.trim();
  if (city) getWeather(city);
});

// ⭐ Handle favorite clicks
favoriteList.addEventListener("click", (e) => {
  if (e.target.tagName === "LI") {
    const selectedCity = e.target.textContent;
    getWeather(selectedCity);
  }
});

// 💾 Add to favorites
addFavoriteBtn.addEventListener("click", () => {
  if (currentCity && !isFavorite(currentCity)) {
    saveFavorite(currentCity);
    renderFavorites();
  }
});

// 🌤️ Fetch current weather
function getWeather(city) {
  fetch(`https://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=${apiKey}`)
    .then(res => res.json())
    .then(data => {
      if (data.cod !== 200) {
        alert("City not found!");
        return;
      }

      currentCity = data.name;
      cityNameEl.textContent = `${data.name}, ${data.sys.country}`;
      temperatureEl.textContent = `🌡️ ${data.main.temp} °C`;
      conditionEl.textContent = `⛅ ${data.weather[0].description}`;
      humidityEl.textContent = `💧 Humidity: ${data.main.humidity}%`;
      windEl.textContent = `💨 Wind: ${data.wind.speed} m/s`;

      changeBackground(data.weather[0].main);
      weatherDisplay.classList.remove("hidden");

      getForecast(city);
    })
    .catch(() => alert("Error fetching weather data"));
}

// 📅 Fetch 5-day forecast
function getForecast(city) {
  fetch(`https://api.openweathermap.org/data/2.5/forecast?q=${city}&units=metric&appid=${apiKey}`)
    .then(res => res.json())
    .then(data => {
      forecastCards.innerHTML = "";
      const dailyData = {};

      data.list.forEach(entry => {
        const date = entry.dt_txt.split(" ")[0];
        if (!dailyData[date]) dailyData[date] = entry;
      });

      Object.keys(dailyData).slice(0, 5).forEach(date => {
        const item = dailyData[date];
        const card = document.createElement("div");
        card.classList.add("forecast-day");
        card.innerHTML = `
          <h4>${new Date(date).toDateString()}</h4>
          <p>🌡️ ${item.main.temp} °C</p>
          <p>🌤️ ${item.weather[0].description}</p>
        `;
        forecastCards.appendChild(card);
      });

      forecastContainer.classList.remove("hidden");
    });
}

// 🌄 Change background image by weather
function changeBackground(condition) {
  const body = document.body;
  switch (condition.toLowerCase()) {
    case "clear":
      body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?clear-sky,blue')";
      break;
    case "clouds":
      body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?cloudy,sky')";
      break;
    case "rain":
      body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?rain,city')";
      break;
    case "snow":
      body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?snow,trees')";
      break;
    case "thunderstorm":
      body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?thunderstorm')";
      break;
    default:
      body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?weather,sky')";
  }
}

// 💾 Save & Retrieve Favorite Cities
function saveFavorite(city) {
  let favorites = JSON.parse(localStorage.getItem("favorites")) || [];
  favorites.push(city);
  localStorage.setItem("favorites", JSON.stringify(favorites));
}

function isFavorite(city) {
  let favorites = JSON.parse(localStorage.getItem("favorites")) || [];
  return favorites.includes(city);
}

function renderFavorites() {
  favoriteList.innerHTML = "";
  let favorites = JSON.parse(localStorage.getItem("favorites")) || [];

  // 🏙️ Show favorite cities as clickable list
  favorites.forEach(city => {
    const li = document.createElement("li");
    li.textContent = city;
    favoriteList.appendChild(li);
  });

  // 🧭 Automatically show weather for first favorite on load
  if (favorites.length > 0 && !currentCity) {
    getWeather(favorites[0]);
  }
}

// 🧠 Initialize favorites when page loads
renderFavorites();
