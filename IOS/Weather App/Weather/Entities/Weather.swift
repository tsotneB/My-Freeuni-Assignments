//
//  Weather.swift
//  Weather
//
//  Created by Tsotne Babunashvili on 18.02.22.
//

import Foundation

struct Forecast: Codable {
    let list : [WeatherInfo]
}

struct WeatherInfo: Codable {
    let weather: [Weather]
    let main: MainInfo
    let wind: WindInfo
    let clouds: CloudsInfo
    let dt: Int64
    let sys: Country
    let name: String?
    let dt_txt: String?
}

struct Coordinate: Codable {
    let lon: Double
    let lat: Double
}

struct Weather: Codable {
    let main: String
    let icon: String
}

struct MainInfo: Codable {
    let temp: Double
    let pressure: Double
    let humidity: Double
}

struct WindInfo: Codable {
    let speed: Double
    let deg: Double
    let gust: Double
}

struct CloudsInfo: Codable {
    let all: Double
}

struct Country: Codable {
    let country: String?

}
