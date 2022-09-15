//
//  Service.swift
//  Weather
//
//  Created by Tsotne Babunashvili on 18.02.22.
//

import Foundation

class Service {
    private let apiKey = "30f9870c2f2bfc6ceb81a31f5f7c00c9"
    var components = URLComponents()
    
    init() {
        components.scheme = "https"
        components.host = "api.openweathermap.org"
       
    }
    
    func loadCurrentWeather(latitude: Double, longitude: Double, completion: @escaping (Result<WeatherInfo, Error>) -> ()) {
        components.path = "/data/2.5/weather"
        let parameters = [
            "appid" : apiKey.description,
            "lat" : latitude.description,
            "lon": longitude.description,
            "units": "metric"
        ]
        
        components.queryItems = parameters.map{key, value in
            return URLQueryItem(name: key, value: value)
        }
        print(components.url?.absoluteString)
        if let url = components.url {
            let request = URLRequest(url: url)
            let task = URLSession.shared.dataTask(with: request, completionHandler: { data, response, error in
                if let error = error {
                    completion(.failure(error))
                    return
                }
                if let data = data {
                    let decoder = JSONDecoder()
                    do {
                        let result = try decoder.decode(WeatherInfo.self, from: data)
                        completion(.success(result))
                    } catch {
                        //  completion(.failure("va"))
                    }
                }
                
            })
            task.resume()
            
        }   else {
        }
    }
    
    func load5DayForecast(latitude: Double, longitude: Double, completion: @escaping (Result<[WeatherInfo], Error>) -> ()) {
        components.path = "/data/2.5/forecast"
        let parameters = [
            "appid" : apiKey.description,
            "lat" : latitude.description,
            "lon": longitude.description,
            "units" : "metric"
        ]
        
        components.queryItems = parameters.map{key, value in
            return URLQueryItem(name: key, value: value)
        }
        if let url = components.url {
            let request = URLRequest(url: url)
            let task = URLSession.shared.dataTask(with: request, completionHandler: { data, response, error in
                if let error = error {
                    completion(.failure(error))
                    return
                }
                if let data = data {
                    let decoder = JSONDecoder()
                    do {
                        let result = try decoder.decode(Forecast.self, from: data)
                        completion(.success(result.list))
                    } catch {
                        completion(.failure(error))
                    }
                }   else {
                    completion(.failure(ServiceError.noData))
                }
                
            })
            task.resume()
            
        }   else {
            completion(.failure(ServiceError.invalidParameters))
        }
    }
    
    

}
    
enum ServiceError: Error {
    case noData
    case invalidParameters
}
// api.openweathermap.org/data/2.5/weather?lat=42.2662&lon=42.7180&appid=30f9870c2f2bfc6ceb81a31f5f7c00c9
