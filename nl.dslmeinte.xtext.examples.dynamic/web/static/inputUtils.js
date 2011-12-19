var InputUtils = {

  /**
   * Getter for Boolean fields.
   */
  bGet: function (id) {
    if( this.radioButtonYes(id + '_') ) {
      return true;
    } else if( this.radioButtonNo(id + '_') ) {
      return false;
    } else {
      return null;
    }
  },

  /**
   * Getter for Enumeration fields.
   */
  eGet: function (id) {
    var select = $(id).childElements()[2].childElements()[0];
    return select.options[select.selectedIndex].value;
  },

  /**
   * Getter for regular (i.e., textual) fields.
   */
  rGet: function (id) {
    return $(id).childElements()[2].childElements()[0].value;
  },

  display: function (id, bValue) {
    var div = $(id);
    if( bValue ) {
      div.show();
    } else {
      div.hide();
    }
  },

  /**
   * Returns whether the "Yes" radio button of the generated boolean input field,
   * indicated through its path, is checked.
   */
  radioButtonYes: function (path)
  {
    return !! $( (path + '2' ) ).checked;
  },

  /**
   * Returns whether the "No" radio button of the generated boolean input field,
   * indicated through its path, is checked.
   */
  radioButtonNo: function (path)
  {
    return !! $( (path + '3' ) ).checked;
  },

  /**
   * Listens to the standard radio button style and executes the given
   * function in case it's changed.
   */
  listenToRadio: function (path, func)
  {
    /* Needs to be onclick instead for IE;
     * see e.g. http://webbugtrack.blogspot.com/2007/11/bug-193-onchange-does-not-fire-properly.html .
     */
    $(path + '2').onclick = func;
    $(path + '3').onclick = func;
  },

  /**
   * Sets or unsets requiredness of a generated input field.
   */
  setMandatory: function (path, mandatory)
  {
    var ancestors = $(path).ancestors();
    ancestors[0].className = "value" + ( mandatory ? " requiredProperty" : "" );
    ancestors[1].select( '.required' ).first().innerHTML = ( mandatory ? "*" : "" );
  },

  /**
   * Returns whether the array contains the value passed.
   */
  contains: function (array, value)
  {
	  return array.find( function(val) { return val == value } );
  }

}