package gosale

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"time"
)

type CouponsByLocation struct {
	BusinessName        string
	Category_name       string
	Subcategory_name    string
	Id                  string
	Revision            string
	Product             string
	Category            string
	Subcategory         string
	Qr_channel          string
	Fb_channel          string
	Twitter_channel     string
	Pinterest_channel   string
	Website_channel     string
	Facebook_budget     string
	Partner_id          string
	Partner             string
	Type                string
	Discount            string
	Pos_text            string
	Punches_needed      string
	Punches_reward      string
	Remaining           string
	Expires             string
	Sku                 string
	Volume              string
	Popularity          string
	Redeem_style        string
	Accepted_businesses string
	Accepted_locations  string
	Image               string
	Url                 string
	Pocketcents_channel string
	Date_added          string
	Upc                 string
	ProductName         string
	Description         string
	Details             string
	Id_coupon_id        string
	Id_location_id      string
	Id_business_id      string
	Id_product_id       string
	Id_category_id      string
	Id_subcategory_id   string
}

func EmptyCouponsByLocation() *CouponsByLocation {
	return &CouponsByLocation{}
}

type ThisCouponsByLocation struct {
	couponsByLocation []CouponsByLocation
}

func CouponsByLocationInit(lat, lon, radius float64) (*ThisCouponsByLocation, error) {
	config, err := NewConfig()
	if err != nil {
		fmt.Println(err)
		return &ThisCouponsByLocation{}, err
	}

	url := fmt.Sprintf("http://%s%s/?method=get_coupons_by_location&lat=%.2f&lon=%.2f&radius=%.2f&OUTPUT=json&API_KEY=%s", config.Service, config.Base_url, lat, lon, radius, config.APIKEY)
	fmt.Println(url)

	result, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return &ThisCouponsByLocation{}, err
	}

	body, err := ioutil.ReadAll(result.Body)
	if err != nil {
		fmt.Println(err)
		return &ThisCouponsByLocation{}, err
	}

	var this ThisCouponsByLocation
	err = json.Unmarshal(body, &this.couponsByLocation)
	if err != nil {
		fmt.Println(err)
		return &ThisCouponsByLocation{}, err
	}

	ref := "2006-01-02 15:04:05"
	var this2 ThisCouponsByLocation
	for _, b := range this.couponsByLocation {
		date, err := time.Parse(ref, b.Expires)
		if err != nil {
			fmt.Println(err)
			return &ThisCouponsByLocation{}, err
		}

		if date.After(time.Now()) {
			this2.couponsByLocation = append(this2.couponsByLocation, b)
		}
	}

	return &this2, nil
}

func (this *ThisCouponsByLocation) Get(next int) (*CouponsByLocation, error) {
	if next < 0 {
		return &CouponsByLocation{}, errors.New("Can't go below 0")
	}

	if next > len(this.couponsByLocation) {
		return &CouponsByLocation{}, errors.New("Can't go above length of array")
	}

	return &this.couponsByLocation[next], nil
}

func (this *ThisCouponsByLocation) Size() int {
	return len(this.couponsByLocation)
}
