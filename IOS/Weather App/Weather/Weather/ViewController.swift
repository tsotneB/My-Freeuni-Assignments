//
//  ViewController.swift
//  Weather
//
//  Created by Tsotne Babunashvili on 18.02.22.
//

import UIKit
import SDWebImage
import CoreLocation
class ViewController: UIViewController {

    
    @IBOutlet private var mainWeatherIcon : UIImageView!
    @IBOutlet private var cityLabel : UILabel!
    @IBOutlet private var weatherLabel : UILabel!
    @IBOutlet private var cloudinessLabel : UILabel!
    @IBOutlet private var pressureLabel : UILabel!
    @IBOutlet private var humidityLabel : UILabel!
    @IBOutlet private var velocityLabel : UILabel!
    @IBOutlet private var directionLabel : UILabel!
    @IBOutlet private var navigation : UINavigationItem!
    @IBOutlet private var loader : UIActivityIndicatorView!
    @IBOutlet private var blurr : UIVisualEffectView!
    @IBOutlet private var errorImage : UIImageView!
    @IBOutlet private var errorLabel : UILabel!
    @IBOutlet private var errorButton : UIButton!
    
    
    private let service = Service()
    private var currentDayWeather : WeatherInfo?
    private var fiveDayForecast : [WeatherInfo?] = []
    
    private var locationManager : CLLocationManager?
    private var forecasts: [WeatherInfo] = []
    private var headerDays: [String] = []
    private let WeekDays = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.loader.isHidden = true
        self.errorButton.isHidden = true
        self.errorLabel.isHidden = true
        self.errorImage.isHidden = true
        locationManager = CLLocationManager()
        locationManager?.delegate = self
        locationManager?.requestAlwaysAuthorization()
        self.setUpNavigation()
   //     self.renderOneDayWeather(lat: locationManager?.location?.coordinate.latitude, lon: locationManager?.location?.coordinate.longitude)
        
    }

    func setUpNavigation() {
        let refresh = UIBarButtonItem(barButtonSystemItem: .refresh, target: self, action: #selector(reload))
        self.navigation.leftBarButtonItem = refresh
        
        let share  = UIBarButtonItem(barButtonSystemItem: .action, target: self, action: #selector(share))
        self.navigation.rightBarButtonItem = share
    }

    @IBAction func refreshData(sender: UIButton){
        renderOneDayWeather(lat:self.locationManager?.location?.coordinate.latitude,lon:self.locationManager?.location?.coordinate.longitude)
    }
    @objc private func share(sender: UIButton) {
        let activityViewController = UIActivityViewController(activityItems: [self.weatherLabel.text], applicationActivities: nil)
        present(activityViewController,animated: true)
    }
    @objc private func reload(sender: UIButton){
        renderOneDayWeather(lat:self.locationManager?.location?.coordinate.latitude,lon:self.locationManager?.location?.coordinate.longitude)
    }

    func renderOneDayWeather(lat : Double?, lon: Double?) {
        self.navigation.rightBarButtonItem?.isEnabled = false
        self.loader.isHidden = false
        self.loader.startAnimating()
        self.blurr.isHidden = false
        self.errorImage.isHidden = true
        self.errorLabel.isHidden = true
        self.errorButton.isHidden = true
        print(lat!)
        print(lon!)
        service.loadCurrentWeather(latitude: lat!, longitude: lon!) { result in
            switch result {
            case .success(let Weather):
                self.renderFirstPage(weather: Weather)
            case .failure(let error):
                DispatchQueue.main.async {
                    self.errorImage.isHidden = false
                    self.errorLabel.isHidden = false
                    self.errorLabel.text = error.localizedDescription
                    self.errorButton.isHidden = false
                    self.loader.stopAnimating()
                    self.loader.isHidden = true
                }
            }
        }
    }
    
    func renderFirstPage(weather: WeatherInfo) {
        let url = URL(string: "https://openweathermap.org/img/wn/" + weather.weather[0].icon + ".png")
        SDWebImageManager.shared.loadImage(with: url, options: .highPriority) { _, _, _ in }
            completed : {image, data, error, cacheType, finished, url in
            if finished{
                if image != nil {
                    DispatchQueue.main.async {
                        self.loader.stopAnimating()
                        self.errorImage.isHidden = true
                        self.errorLabel.isHidden = true
                        self.errorButton.isHidden = true
                        self.navigation.rightBarButtonItem?.isEnabled = true
                        self.blurr.isHidden = true
                        self.loader.isHidden = true
                        self.mainWeatherIcon.image = image
                        self.updateOneDayWeatherLabels(weather: weather)
                    }
                }
            }
        }
    }
    
    func updateOneDayWeatherLabels(weather : WeatherInfo) {
        self.cityLabel.text = weather.name! + ", " + weather.sys.country!
        self.weatherLabel.text = weather.main.temp.description + "°C | " + weather.weather[0].main
        self.cloudinessLabel.text = weather.clouds.all.description + " %"
        self.humidityLabel.text = weather.main.humidity.description + " mm"
        self.pressureLabel.text = weather.main.pressure.description + " hPa"
        self.velocityLabel.text = weather.wind.speed.description + " km/h"
        self.directionLabel.text = self.getDirection(degree : weather.wind.deg)
    }
    func getDirection(degree : Double) -> String {
        if (degree >= 22.5 && degree < 67.5) {
            return "NE"
        }
        if (degree >= 67.5 && degree < 112.5) {
            return "E"
        }
        if (degree >= 112.5 && degree < 157.5) {
            return "SE"
        }
        if (degree >= 157.5 && degree < 202.5) {
            return "S"
        }
        if (degree >= 202.5 && degree < 247.5) {
            return "SW"
        }
        if (degree >= 247.5 && degree < 292.5) {
            return "W"
        }
        if (degree >= 292.5 && degree < 337.5) {
            return "NW"
        }
        return "N"
    }
}


extension ViewController: CLLocationManagerDelegate {
  func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
    switch status {
    case .authorizedWhenInUse:
        locationManager?.requestLocation()
    case .authorizedAlways:
      locationManager?.requestLocation()
    default:
      print("No Location")
    }
  }

  func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
    locations.forEach { (location) in
        print("s")
        self.renderOneDayWeather(lat: location.coordinate.latitude, lon: location.coordinate.longitude)
    }
  }

  func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
    if let error = error as? CLError, error.code == .denied {
       locationManager?.stopMonitoringSignificantLocationChanges()
       return
    }
  }
}

// api.openweathermap.org/data/2.5/weather?lat=42.2662&lon=42.7180&appid=30f9870c2f2bfc6ceb81a31f5f7c00c9
