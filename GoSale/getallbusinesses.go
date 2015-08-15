package gosale

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/nbjahan/gofuzz"
)

type AllBusinesses struct {
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

type ThisAllBusinesses struct {
	allBusinesses      []AllBusinesses
	filteredBusinesses []AllBusinesses
}

func AllBusinessesInit() (*ThisAllBusinesses, error) {
	config, err := NewConfig()
	if err != nil {
		fmt.Println(err)
		return &ThisAllBusinesses{}, err
	}

	url := fmt.Sprintf("http://%s%s/?method=get_all_businesses&OUTPUT=json&API_KEY=%s", config.Service, config.Base_url, config.APIKEY)
	fmt.Println(url)

	result, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return &ThisAllBusinesses{}, err
	}

	body, err := ioutil.ReadAll(result.Body)
	if err != nil {
		fmt.Println(err)
		return &ThisAllBusinesses{}, err
	}

	var this ThisAllBusinesses
	err = json.Unmarshal(body[1:], &this.allBusinesses)
	if err != nil {
		fmt.Println(err)
		return &ThisAllBusinesses{}, err
	}

	return &this, nil
}

func (this *ThisAllBusinesses) Get(next int) (*AllBusinesses, error) {
	if next < 0 {
		return &AllBusinesses{}, errors.New("Can't go below 0")
	}

	if next > len(this.allBusinesses) {
		return &AllBusinesses{}, errors.New("Can't go above length of array")
	}

	return &this.allBusinesses[next], nil
}

func (this *ThisAllBusinesses) Size() int {
	return len(this.allBusinesses)
}

func (this *ThisAllBusinesses) Search(pattern string) {
	m := gofuzz.New()
	this.filteredBusinesses = nil

	for _, business := range this.allBusinesses {
		pos, score := m.Search(business.Name, pattern, 0)
		if pos == 0 {
			if score == 0 {
				this.filteredBusinesses = append(this.filteredBusinesses, business)
			}
		}
	}
}

func (this *ThisAllBusinesses) GetFiltered(next int) (*AllBusinesses, error) {
	if next < 0 {
		return &AllBusinesses{}, errors.New("Can't go below 0")
	}

	if next > len(this.filteredBusinesses) {
		return &AllBusinesses{}, errors.New("Can't go above length of array")
	}

	return &this.filteredBusinesses[next], nil
}

func (this *ThisAllBusinesses) SizeFiltered() int {
	return len(this.filteredBusinesses)
}
