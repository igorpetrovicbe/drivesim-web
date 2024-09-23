import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserHomepageService } from '../services/user-homepage.service';
import { UserHomepageInfo } from '../user-homepage-info';
import { Role } from '../role';

@Component({
    selector: 'app-user-homepage',
    templateUrl: './user-homepage.component.html',
    styleUrls: ['./user-homepage.component.css'],
})
export class UserHomepageComponent implements OnInit {
    userInfo!: UserHomepageInfo;
    errorMsg = '';

    uploadedImage!: Blob;
    angle = '';
    speed = '';
    frameNumber!: number;
    cropImage = true;
    modelSize = "MEDIUM";

    base64Images: string[] = [];
    currentImage: string = '';
    imageIndex: number = 0;
    animationInterval: any;

    result = null;
    resultErrorMessage = '';

    generationRequestAccepted = false;

    loadUserFailed = true;

    constructor(private router: Router, private _userHomepageService: UserHomepageService) {}

    ngOnInit(): void {
        this.userInfo = new UserHomepageInfo('', '', 0);

        this._userHomepageService.getUserData().subscribe((data) => {
            this.userInfo = data;
            this.loadUserFailed = false;
            console.log(this.userInfo);
        }, (error) => {
            if(error.status == 401){
                this.router.navigate(['/login']);
            }
        });
    }

    onFileSelected(event: any): void {
        const file = event.target.files[0]; // Get the selected file
        if (file) {
            this.uploadedImage = file; // Ensure it's assigned to a valid file object
            console.log('File selected:', this.uploadedImage); // This should log the file object
        } else {
            console.error('No file selected');
        }
    }

    validate(): boolean {
        // Check if image is provided
        if (!this.uploadedImage) {
            //this.errorMsg =  "Image cannot be null";
            return false;
        }

        // Validate framesToGenerate
        const framesToGenerate = this.frameNumber;
        if (isNaN(framesToGenerate) || framesToGenerate < 1 || framesToGenerate > 100) {
            //this.errorMsg =  "Frames to generate must be between 1 and 100";
            return false;
        }

        // Validate angle
        const angleValue = parseFloat(this.angle);
        if (isNaN(angleValue) || angleValue < -482.4 || angleValue > 394.3) {
            //this.errorMsg =  "Angle must be between -482.4 and 394.3";
            return false;
        }

        // Validate speed
        const speedValue = parseFloat(this.speed);
        if (isNaN(speedValue) || speedValue < -0.6471078 || speedValue > 46.590874) {
            //this.errorMsg =  "Speed must be between -0.6471078 and 46.590874";
            return false;
        }

        // Validate cropImage
        if (this.cropImage == null) {
            //this.errorMsg =  "Crop Image flag cannot be null";
            return false;
        }

        // Validate modelSize
        if (!this.modelSize) {
            //this.errorMsg =  "Model size cannot be null";
            return false;
        }

        return true; // All validations passed
    }

    generate() {
        if (!this.validate()) {
            return;
        }

        const reader = new FileReader();
        reader.onload = () => {
            // Convert image to base64
            let base64Image = reader.result as string;

            // Remove the "data:image/png;base64," or any similar prefix
            base64Image = base64Image.replace(/^data:image\/[a-z]+;base64,/, '');

            // Prepare the JSON data
            const jsonData = {
                angle: this.angle,
                speed: this.speed,
                framesToGenerate: this.frameNumber,
                cropImage: this.cropImage,
                modelSize: this.modelSize,
                image: base64Image // Only the base64 encoded image data, without prefix
            };

            // Send the JSON data to the backend
            this._userHomepageService.generate(jsonData).subscribe(
                (response: any) => {
                    this.generationRequestAccepted = true;
                    console.log(response);
                },
                (error: any) => {
                    this.errorMsg = error.error;
                }
            );
        };

        // Read the uploaded image as a base64 string
        reader.readAsDataURL(this.uploadedImage);
    }

    getResults(): void {
        this._userHomepageService.getResult().subscribe((data) => {
            if (typeof data === 'string'){
                this.resultErrorMessage = data;
                return;
            }
            this.result = data;
    
            // Ensure base64 strings have the correct prefix before display
            this.base64Images = data.framesBase64.map((base64: string) => `data:image/png;base64,${base64}`);
            
            console.log(this.result);
            this.resultErrorMessage = '';
    
            // Start the animation with the properly formatted base64 images
            this.startAnimation();
        }, (error) => {
            this.resultErrorMessage = error.error;
            console.error('Error fetching results', error);
        });
    }
    
    calculateGemCost(): number {
        let smallMultipler = 1;
        let mediumMultiplyer = 3;
        let largeMultiplyer = 12;
        if (this.modelSize == "SMALL")
            return this.frameNumber * smallMultipler;
        else if (this.modelSize == "MEDIUM")
            return this.frameNumber * mediumMultiplyer;
        else
            return this.frameNumber * largeMultiplyer;
    }

    // Function to start the image loop animation
    startAnimation() {
        if (this.base64Images.length > 0) {
            this.currentImage = this.base64Images[0];  // Display the first image initially
            this.animationInterval = setInterval(() => {
                this.imageIndex = (this.imageIndex + 1) % this.base64Images.length;
                this.currentImage = this.base64Images[this.imageIndex];
            }, 40);
        }
    }

    // Cleanup the interval when the component is destroyed
    ngOnDestroy(): void {
        if (this.animationInterval) {
            clearInterval(this.animationInterval);
        }
    }
    
}
