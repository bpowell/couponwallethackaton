package gosale

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
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

var couponsByLocation []CouponsByLocation
var CouponsByLocationSize int

func GetCouponsByLocation(lat, lon, radius float64) {
	config, err := NewConfig()
	if err != nil {
		fmt.Println(err)
		return
	}

	url := fmt.Sprintf("http://%s%s/?method=get_coupons_by_location&lat=%.2f&lon=%.2f&radius=%.2f&OUTPUT=json&API_KEY=%s", config.Service, config.Base_url, lat, lon, radius, config.APIKEY)
	fmt.Println(url)

	result, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return
	}

	body, err := ioutil.ReadAll(result.Body)
	if err != nil {
		fmt.Println(err)
		return
	}

	err = json.Unmarshal(body, &couponsByLocation)
	if err != nil {
		fmt.Println(err)
		return
	}

	CouponsByLocationSize = len(couponsByLocation)
}

func Get(next int) CouponsByLocation {
	return couponsByLocation[next]
}
