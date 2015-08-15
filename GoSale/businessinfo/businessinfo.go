package businessinfo

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/bpowell/couponwallethackaton/gosale/cwconfig"
)

type BusinessInfo struct {
	Id                     string
	Partner_id             string
	Partner                string
	Name                   string
	Logo                   string
	Package                string
	Package_price          string
	Package_expires        string
	StripeToken            string
	Location_limit         string
	Additional_locations   string
	Location_price         string
	Coupon_limit           string
	Additional_coupons     string
	Coupon_price           string
	Free_coupons           string
	Weekly_reporting       string
	Weekly_reporting_price string
	Daily_reporting        string
	Daily_reporting_price  string
	Paper_reporting        string
	Paper_reporting_price  string
	Customer_loyalty       string
	Customer_loyalty_price string
	Discount_code          string
	Id_business_id         string
}

func Init(id int) (*BusinessInfo, error) {
	config, err := cwconfig.NewConfig()
	if err != nil {
		fmt.Println(err)
		return &BusinessInfo{}, err
	}

	url := fmt.Sprintf("http://%s%s/?method=get_business_info&b=%d&OUTPUT=json&API_KEY=%s", config.Partner, config.Base_url, id, config.APIKEY)
	fmt.Println(url)

	result, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return &BusinessInfo{}, err
	}

	body, err := ioutil.ReadAll(result.Body)
	if err != nil {
		fmt.Println(err)
		return &BusinessInfo{}, err
	}

	var businessInfo BusinessInfo
	err = json.Unmarshal(body, &businessInfo)
	if err != nil {
		fmt.Println(err)
		return &BusinessInfo{}, err
	}

	return &businessInfo, nil
}
