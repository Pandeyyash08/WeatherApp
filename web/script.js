const API_KEY = "28f61f4b78b8f9cc89e00e40149d3448";
const BASE_URL = "https://api.openweathermap.org/data/2.5";

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

// 🌤️ Fetch weather data
async function getWeather(city) {
    try {
        const [weatherResponse, forecastResponse] = await Promise.all([
            fetch(`${BASE_URL}/weather?q=${encodeURIComponent(city)}&appid=${API_KEY}&units=metric`),
            fetch(`${BASE_URL}/forecast?q=${encodeURIComponent(city)}&appid=${API_KEY}&units=metric`)
        ]);

        if (!weatherResponse.ok || !forecastResponse.ok) {
            throw new Error('City not found');
        }

        const weatherData = await weatherResponse.json();
        const forecastData = await forecastResponse.json();

        displayCurrentWeather(weatherData);
        displayForecast(forecastData);
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
        weatherDisplay.classList.add("hidden");
        forecastContainer.classList.add("hidden");
    }
}

function displayCurrentWeather(data) {
    console.log('Current Weather Data:', data);
    
    currentCity = data.name;
    const country = data.sys.country;
    const temp = Math.round(data.main.temp);
    const weather = data.weather[0].main;
    const description = data.weather[0].description;
    const humidity = data.main.humidity;
    const windSpeed = data.wind.speed;

    cityNameEl.textContent = `${currentCity}, ${country}`;
    temperatureEl.textContent = `🌡️ ${temp}°C`;
    conditionEl.textContent = `⛅ ${description.charAt(0).toUpperCase() + description.slice(1)}`;
    humidityEl.textContent = `💧 Humidity: ${humidity}%`;
    windEl.textContent = `💨 Wind: ${windSpeed} m/s`;

    weatherDisplay.classList.remove("hidden");
    changeBackground(weather);
}

function displayForecast(data) {
    console.log('Forecast Data:', data);
    
    forecastCards.innerHTML = '';
    
    // Get one forecast per day (at 12:00)
    const dailyForecasts = data.list.filter(item => item.dt_txt.includes('12:00:00')).slice(0, 5);

    dailyForecasts.forEach(forecast => {
        const date = new Date(forecast.dt * 1000);
        const temp = Math.round(forecast.main.temp);
        const weather = forecast.weather[0].description;
        const humidity = forecast.main.humidity;
        const wind = forecast.wind.speed;
        const icon = forecast.weather[0].icon;

        const card = document.createElement("div");
        card.className = "forecast-card";
        card.innerHTML = `
            <h3>${date.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' })}</h3>
            <img src="https://openweathermap.org/img/wn/${icon}@2x.png" alt="${weather}">
            <p class="temp">🌡️ ${temp}°C</p>
            <p class="weather">⛅ ${weather.charAt(0).toUpperCase() + weather.slice(1)}</p>
            <p class="humidity">💧 Humidity: ${humidity}%</p>
            <p class="wind">💨 Wind: ${wind} m/s</p>
        `;
        forecastCards.appendChild(card);
    });

    forecastContainer.classList.remove("hidden");
}

// 🌄 Change background image by weather
function changeBackground(condition) {
    const body = document.body;
    const weather = condition.toLowerCase();
    
    if (weather === "clear") {
        body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?clear-sky,blue')";
    } else if (weather === "clouds") {
        body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?cloudy,sky')";
    } else if (weather === "rain" || weather === "drizzle") {
        body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?rain,city')";
    } else if (weather === "snow") {
        body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?snow,trees')";
    } else if (weather === "thunderstorm") {
        body.style.backgroundImage = "url('https://source.unsplash.com/1600x900/?thunderstorm')";
    } else {
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

    favorites.forEach(city => {
        const li = document.createElement("li");
        li.textContent = city;
        favoriteList.appendChild(li);
    });

    if (favorites.length > 0 && !currentCity) {
        getWeather(favorites[0]);
    }
}

// Initialize favorites when page loads
renderFavorites();
