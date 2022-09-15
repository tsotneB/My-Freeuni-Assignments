//
//  ForecastViewController.swift
//  Weather
//
//  Created by Tsotne Babunashvili on 21.02.22.
//

import UIKit
import SDWebImage
import CoreData
class ForecastViewController: UIViewController {
    @IBOutlet private var forecast : UITableView!
    @IBOutlet private var navigation: UINavigationItem!
    @IBOutlet private var loader: UIActivityIndicatorView!
    @IBOutlet private var blurr: UIVisualEffectView!
    @IBOutlet private var errorImage : UIImageView!
    @IBOutlet private var errorLabel : UILabel!
    @IBOutlet private var errorButton : UIButton!
    private var locationManager : CLLocationManager?

    
    private let WeekDays = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
    private var days : [String] = []
    private var weatherForecast : [String : [WeatherInfo]] = [:]
    private let service = Service()
    private var images : [String : UIImage] = [:]
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setUpNavigation()
        forecast.delegate = self
        forecast.dataSource = self
        forecast.register(UINib(nibName: "ForecastCell", bundle: nil),forCellReuseIdentifier:"ForecastCell")
        locationManager = CLLocationManager()
        locationManager?.delegate = self
        locationManager?.requestAlwaysAuthorization()
        
    }
    
    func setUpNavigation() {
        let refresh = UIBarButtonItem(barButtonSystemItem: .refresh, target: self, action: #selector(reload))
        self.navigation.leftBarButtonItem = refresh
    }
    
    @objc private func reload(sender: UIButton){
        retrieveData(lat: self.locationManager?.location?.coordinate.latitude, lon: self.locationManager?.location?.coordinate.longitude)
    }
    func retrieveData(lat : Double?, lon: Double?) {
        self.loader.isHidden = false
        self.blurr.isHidden = false
        self.loader.startAnimating()
        self.errorImage.isHidden = true
        self.errorLabel.isHidden = true
        self.errorButton.isHidden = true
        service.load5DayForecast(latitude: lat!, longitude: lon!) { result in
            switch result {
            case .success(let Weathers):
                self.weatherForecast = [:]
                self.days = []
                self.images = [:]
                Weathers.sorted(by: {$0.dt < $1.dt}).forEach( { (item) in
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss"
                    let date = dateFormatter.date(from: item.dt_txt!)
                    let calendar = Calendar.current
                    let day = calendar.component(.weekday, from: date!)
                    
                    let weekDay = self.WeekDays[day-1]
                    if !self.days.contains(weekDay) {
                        self.days.append(weekDay)
                    }
                    if self.weatherForecast[weekDay] != nil {
                        self.weatherForecast[weekDay]?.append(item)
                    }   else {
                        var weather : [WeatherInfo] = []
                        weather.append(item)
                        self.weatherForecast[weekDay] = weather
                    }
                    let url = URL(string: "https://openweathermap.org/img/wn/" + item.weather[0].icon + ".png")
                    SDWebImageManager.shared.loadImage(with: url, options: .highPriority) { _, _, _ in }
                        completed : {image, data, error, cacheType, finished, url in
                            if finished{
                                if image != nil {
                                    DispatchQueue.main.async {
                                        self.images[item.dt_txt!] = image
                                        self.loader.stopAnimating()
                                        self.loader.isHidden = true
                                        self.blurr.isHidden = true
                                        self.forecast.reloadData()
                                    }
                                }
                            }
                        }
                })
            case .failure(let error):
                DispatchQueue.main.async {
                    self.errorImage.isHidden = false
                    self.errorLabel.isHidden = false
                    self.errorButton.isHidden = false
                    self.errorLabel.text = error.localizedDescription
                    self.loader.stopAnimating()
                    self.loader.isHidden = true
                }
            }
        }
    }
    
    @IBAction func refreshData(sender: UIButton){
        self.retrieveData(lat: locationManager?.location?.coordinate.latitude, lon: locationManager?.location?.coordinate.longitude)
    }
}



extension ForecastViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // if !self.onlyFavs && contact.isFavourite
        return self.weatherForecast[self.days[section]]!.count
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return self.days.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let tableViewCell = tableView.dequeueReusableCell(withIdentifier: "ForecastCell", for: indexPath)
        if let cell = tableViewCell as? ForecastCell {
            var currentDay = self.days[indexPath.section]
            var currentHourWeather = self.weatherForecast[currentDay]![indexPath.row]
            cell.tempLabel.text = currentHourWeather.main.temp.description + "°C"
            cell.weatherLabel.text = currentHourWeather.weather[0].main
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss"
            let date = dateFormatter.date(from: currentHourWeather.dt_txt!)
            let calendar = Calendar.current
            let hour = calendar.component(.hour, from: date!)
            //let minute = calendar.component(.minute, from: date!)
            cell.hourLabel.text = hour.description + ":00"
            cell.weatherImage.image = self.images[currentHourWeather.dt_txt!]
            return cell
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UITableViewHeaderFooterView()
        headerView.textLabel?.text = self.days[section]
        headerView.backgroundView = UIView()
        headerView.backgroundView?.backgroundColor = .white
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 40
    }
    
}



extension ForecastViewController: CLLocationManagerDelegate {
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
        self.retrieveData(lat: location.coordinate.latitude, lon: location.coordinate.longitude)
    }
  }

  func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
    if let error = error as? CLError, error.code == .denied {
       locationManager?.stopMonitoringSignificantLocationChanges()
       return
    }
  }
}

