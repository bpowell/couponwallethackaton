package gosale

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"time"
)

type ThisCouponsList struct {
	allCouponsList []CouponsByLocation
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

		var couponList CouponsByLocation
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

func (this *ThisCouponsList) Get(next int) (*CouponsByLocation, error) {
	if next < 0 {
		return &CouponsByLocation{}, errors.New("too small")
	}

	if next > len(this.allCouponsList) {
		return &CouponsByLocation{}, errors.New("too big")
	}

	return &this.allCouponsList[next], nil
}

func (this *ThisCouponsList) Size() int {
	return len(this.allCouponsList)
}
