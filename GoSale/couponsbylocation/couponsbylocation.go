package couponsbylocation

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/bpowell/couponwallethackaton/gosale/cwconfig"
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

type This struct {
	couponsByLocation []CouponsByLocation
}

func Init(lat, lon, radius float64) (*This, error) {
	config, err := cwconfig.NewConfig()
	if err != nil {
		fmt.Println(err)
		return &This{}, err
	}

	url := fmt.Sprintf("http://%s%s/?method=get_coupons_by_location&lat=%.2f&lon=%.2f&radius=%.2f&OUTPUT=json&API_KEY=%s", config.Service, config.Base_url, lat, lon, radius, config.APIKEY)
	fmt.Println(url)

	result, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return &This{}, err
	}

	body, err := ioutil.ReadAll(result.Body)
	if err != nil {
		fmt.Println(err)
		return &This{}, err
	}

	var this This
	err = json.Unmarshal(body, &this.couponsByLocation)
	if err != nil {
		fmt.Println(err)
		return &This{}, err
	}

	return &this, nil
}

func (this *This) Get(next int) (*CouponsByLocation, error) {
	if next < 0 {
		return &CouponsByLocation{}, errors.New("Can't go below 0")
	}

	if next > len(this.couponsByLocation) {
		return &CouponsByLocation{}, errors.New("Can't go above length of array")
	}

	return &this.couponsByLocation[next], nil
}

func (this *This) Size() int {
	return len(this.couponsByLocation)
}
