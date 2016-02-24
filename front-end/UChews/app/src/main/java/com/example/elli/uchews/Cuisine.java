package com.example.elli.uchews;

/**
 * Created by Chris on 2/18/2016.
 */
public enum Cuisine {
    AMERICAN ("348", "American"),
    BBQ ("349", "BBQ"),
    BURGERS ("351", "Burgers"),
    CHINESE ("352", "Chinese"),
    FRENCH ("356", "French"),
    INDIAN ("357", "Indian"),
    ITALIAN ("358", "Italian"),
    JAPANESE ("359", "Japanese"),
    KOREAN ("360", "Korean"),
    MEXICAN ("361","Mexican"),
    MIDDLE_EASTERN ("362", "Middle Eastern"),
    PIZZA ("363", "Pizza"),
    SEAFOOD ("364", "Seafood"),
    SUSHI ("366", "Sushi"),
    THAI ("367", "Thai"),
    VEGETARIAN ("368","Vegetarian"),

    // It may be better to make these ones "Dining Styles" as opposed to cuisines //
    BUFFETS ("350", "Buffets"),
    DELIS ("353", "Delis"),
    DINERS ("354", "Diners"),
    FAST_FOOD ("355", "Fast Food"),
    STEAKHOUSES ("365", "Steakhouses"),
    //---------------------------------------------------------------------------//
    ;

    /**
     * The colloquial name for the cuisine (i.e. Asian).
     */
    private final String name;

    /**
     * The id used to filter factual queries.
     */
    private final String factual_id;

    Cuisine( String factual_id, String name) {
        this.name = name;
        this.factual_id = factual_id;
    }


    public String getName() {
        return name;
    }

    public String getFactual_id() {
        return factual_id;
    }

    public static Cuisine getCuisineById(String cuisine_id){
        for(Cuisine c: Cuisine.values()){
            if(c.factual_id.equals(cuisine_id))
                return c;
        }

        return null;
    }

}
