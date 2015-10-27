module.exports = {
	compile: {
	    options: {
	      templateName: function(sourceFile) {			
	        return sourceFile.replace(/templates\//, '').replace(".hbs", '');
	      },
			isHTMLBars: true
	    },
	    files: [
	      "templates/**/*.hbs"
	    ],
		dest: "dist/templates.js"

	}
};