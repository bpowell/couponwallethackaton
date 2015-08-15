package gosale

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"time"
)

type CouponsList struct {
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

type ThisCouponsList struct {
	allCouponsList []CouponsList
}

func CouponsListInit(pattern string) (*ThisCouponsList, error) {
	config, err := NewConfig()
	if err != nil {
		fmt.Println(err)
		return &ThisCouponsList{}, err
	}

	business, err := AllBusinessesInit()
	if err != nil {
		fmt.Println(err)
		return &ThisCouponsList{}, nil
	}

	var this ThisCouponsList

	business.Search(pattern)
	for _, result := range business.filteredBusinesses {
		url := fmt.Sprintf("http://%s%s/?method=get_coupon_list&b=%s&OUTPUT=json&API_KEY=%s", config.Service, config.Base_url, result.Id_business_id, config.APIKEY)
		fmt.Println(url)

		result, err := http.Get(url)
		if err != nil {
			fmt.Println(err)
			return &ThisCouponsList{}, err
		}

		body, err := ioutil.ReadAll(result.Body)
		if err != nil {
			fmt.Println(err)
			return &ThisCouponsList{}, err
		}

		var couponList CouponsList
		err = json.Unmarshal(body, &couponList)
		if err != nil {
			fmt.Println(err)
			return &ThisCouponsList{}, err
		}

		ref := "2006-01-02 15:04:05"
		date, err := time.Parse(ref, couponList.Expires)
		if err != nil {
			fmt.Println(err)
			continue
		}

		if date.Before(time.Now()) {
			continue
		}

		this.allCouponsList = append(this.allCouponsList, couponList)

	}

	return &this, nil
}

func (this *ThisCouponsList) Get(next int) (*CouponsList, error) {
	if next < 0 {
		return &CouponsList{}, errors.New("too small")
	}

	if next > len(this.allCouponsList) {
		return &CouponsList{}, errors.New("too big")
	}

	return &this.allCouponsList[next], nil
}

func (this *ThisCouponsList) Size() int {
	return len(this.allCouponsList)
}
