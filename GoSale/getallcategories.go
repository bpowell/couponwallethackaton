package gosale

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/nbjahan/gofuzz"
)

type AllCategories struct {
	Category string
}

type ThisAllCategories struct {
	allCategories      []AllCategories
	filteredCategories []AllCategories
}

func AllCategoriesInit() (*ThisAllCategories, error) {
	config, err := NewConfig()
	if err != nil {
		fmt.Println(err)
		return &ThisAllCategories{}, err
	}

	url := fmt.Sprintf("http://%s%s/?method=get_categories&OUTPUT=json&API_KEY=%s", config.Service, config.Base_url, config.APIKEY)
	fmt.Println(url)

	result, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return &ThisAllCategories{}, err
	}

	body, err := ioutil.ReadAll(result.Body)
	if err != nil {
		fmt.Println(err)
		return &ThisAllCategories{}, err
	}

	var this ThisAllCategories
	err = json.Unmarshal(body, &this.allCategories)
	if err != nil {
		fmt.Println(err)
		return &ThisAllCategories{}, err
	}

	return &this, nil
}

func (this *ThisAllCategories) Get(next int) (*AllCategories, error) {
	if next < 0 {
		return &AllCategories{}, errors.New("Can't go below 0")
	}

	if next > len(this.allCategories) {
		return &AllCategories{}, errors.New("Can't go above length of array")
	}

	return &this.allCategories[next], nil
}

func (this *ThisAllCategories) Size() int {
	return len(this.allCategories)
}

func (this *ThisAllCategories) Search(pattern string) {
	m := gofuzz.New()
	this.filteredCategories = nil

	for _, category := range this.allCategories {
		pos, score := m.Search(category.Category, pattern, 0)
		if pos == 0 {
			if score == 0 {
				this.filteredCategories = append(this.filteredCategories, category)
			}
		}
	}
}

func (this *ThisAllCategories) GetFiltered(next int) (*AllCategories, error) {
	fmt.Println(len(this.filteredCategories))
	if next < 0 {
		return &AllCategories{}, errors.New("Can't go below 0")
	}

	if next > len(this.filteredCategories) {
		return &AllCategories{}, errors.New("Can't go above length of array")
	}

	return &this.filteredCategories[next], nil
}

func (this *ThisAllCategories) SizeFiltered() int {
	return len(this.filteredCategories)
}
