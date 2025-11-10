<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Weather Forecast App</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="app">
    <h1>🌦️ Weather Forecast App</h1>

    <div class="top-section">
      <div class="search-box">
        <input type="text" id="cityInput" placeholder="Enter city name...">
        <button id="searchBtn">Search</button>
      </div>

      <div class="favorite-section">
        <h3>⭐ Favorite Cities</h3>
        <ul id="favoriteList"></ul>
      </div>
    </div>

    <div class="content">
      <div id="weatherDisplay" class="weather-card hidden">
        <h2 id="cityName"></h2>
        <p id="temperature"></p>
        <p id="condition"></p>
        <p id="humidity"></p>
        <p id="wind"></p>
        <button id="addFavorite">Add to Favorites</button>
      </div>

      <div id="forecastContainer" class="forecast hidden">
        <h3>5-Day Forecast</h3>
        <div id="forecastCards" class="forecast-grid"></div>
      </div>
    </div>
  </div>

  <script src="script.js"></script>
</body>
</html>
