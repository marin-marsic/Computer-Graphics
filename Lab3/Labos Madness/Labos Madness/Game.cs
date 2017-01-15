using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using System;
using System.Collections;

namespace Bday
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game : Microsoft.Xna.Framework.Game
    {

        enum States {Play, Result};
        /// <summary>
        /// Bottom paddle object
        /// </summary>
        public Bag Bag { get; private set; }
        /// <summary>
        /// Gifts
        /// </summary>
        private ArrayList Gifts = new ArrayList();

        public Gift Special { get; private set; }
        bool specialGift = false;
        bool specialUp = false;
        /// <summary>
        /// Background image
        /// </summary>
        public Background Background { get; private set; }
        /// <summary>
        /// Sound when ball hits a wall.
        /// SoundEffect is a type defined in Monogame framework
        /// Won't be used until further notice.
        /// </summary>
        public SoundEffect HitSound { get; private set; }
        /// <summary>
        /// 1UP sound - collecting bonus life.
        /// </summary>
        public SoundEffect SpecialGoodSound { get; private set; }
        /// <summary>
        /// Sound heard when player misses reward.
        /// </summary>
        public SoundEffect MissSound { get; private set; }
        /// <summary>
        /// Sound heard when user loses all his lifes.
        /// </summary>
        public SoundEffect GameOverfSound { get; private set; }
        /// <summary>
        /// 1Down sound - collecting anti-life.
        /// </summary>
        public SoundEffect SpecialBadSound { get; private set; }
        /// <summary>
        /// Background music. Song is a type defined in Monogame framework
        /// </summary>
        public Song Music { get; private set; }
        /// <summary>
        /// Generic list that holds Sprites that should be drawn on screen
        /// </summary>
        private ArrayList SpritesForDrawList = new ArrayList();
        /// <summary>
        /// Generic list that holds textures of gifts used in game.
        /// </summary>
        ArrayList giftTextures = new ArrayList();
        /// <summary>
        /// Generic list that holds textures of special gifts - 1UP and 1DOWN.
        /// </summary>
        ArrayList specialTextures = new ArrayList();
        ArrayList backgrounds = new ArrayList();
        /// <summary>
        /// Image shown after user loses all his lifes.
        /// </summary>
        public SR sr { get; private set; }
        /// <summary>
        /// Coordinates of label showing current score.
        /// </summary>
        Vector2 ScorePosition = new Vector2(10, 10);
        /// <summary>
        /// Coordinates of label showing lifes remaining.
        /// </summary>
        Vector2 LivesPosition;
        /// <summary>
        /// Coordinates of label: "Press space to continue".
        /// </summary>
        Vector2 PressSpacePosition;
        /// <summary>
        /// Counter counting to new special gift.
        /// </summary>
        public int specialCounter;
        /// <summary>
        /// Flag saying if the gifts are falling straight or zig-zaging.
        /// </summary>
        bool ZigZag = false;
        /// <summary>
        /// Initial score.
        /// </summary>
        private int Score = 0;
        /// <summary>
        /// Initial number of lifes.
        /// </summary>
        private int Lives = 3;
        /// <summary>
        /// Font used to show text.
        /// </summary>
        private SpriteFont font;

        int backgroundId = 0;
        int numberOfScenes = 3;

        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        System.Random random = new System.Random();

        /// <summary>
        /// Space beetween two gifts.
        /// </summary>
        public int spaceBeetweenGifts = 100;

        /// <summary>
        /// State of game. There are two states: Play and Score.
        /// User starts in Play mode and plays game until he loses all his lifes.
        /// User is then in Result mode from which he can choose to try playing again.
        /// </summary>
        States CurrentState = States.Play;

        public Game()
        {
            graphics = new GraphicsDeviceManager(this)
            {
                PreferredBackBufferHeight = 656,
                PreferredBackBufferWidth = 1000
            };
            Content.RootDirectory = "Content";
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here

            base.Initialize();

            // Position of first special gift is generated randomly.
            specialCounter = random.Next(15, 20);
            
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of our content.
        /// </summary>
        protected override void LoadContent()
        {

            // Initialize new SpriteBatch object which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // Ask graphics device about screen bounds we are using.
            var screenBounds = GraphicsDevice.Viewport.Bounds;

            Texture2D srTexture = Content.Load<Texture2D>("SR");
            sr = new SR(srTexture);
            sr.Position = new Vector2((screenBounds.Right - sr.Size.Width) / 2, (screenBounds.Bottom - sr.Size.Height) / 3);

            PressSpacePosition = new Vector2((screenBounds.Right - sr.Size.Width) / 2, (screenBounds.Bottom - sr.Size.Height) / 3 + 20 + sr.Size.Height);

            Texture2D upTexture = Content.Load<Texture2D>("1up");
            Texture2D downTexture = Content.Load<Texture2D>("bomb");
            specialTextures.Add(upTexture);
            specialTextures.Add(downTexture);

            // Load paddle texture using Content.Load static method
            Texture2D bagTexture = Content.Load<Texture2D>("bag");

            // Create bottom and top paddles and set their initial position
            Bag = new Bag(bagTexture);

            // Position both paddles with help screenBounds object
            Bag.Position = new Vector2((screenBounds.Right - Bag.Size.Width) / 2, screenBounds.Bottom - Bag.Size.Height);

            // Load gift textures
            for (int i = 1; i <= numberOfScenes; i++)
            {
                string id = i.ToString();
                Texture2D giftTexture1 = Content.Load<Texture2D>(id+"/gift1");
                Texture2D giftTexture2 = Content.Load<Texture2D>(id + "/gift2");
                Texture2D giftTexture3 = Content.Load<Texture2D>(id + "/gift3");
                Texture2D giftTexture4 = Content.Load<Texture2D>(id + "/gift4");
                giftTextures.Add(giftTexture1);
                giftTextures.Add(giftTexture2);
                giftTextures.Add(giftTexture3);
                giftTextures.Add(giftTexture4);
            }

            // Load background texture and create a new background object.
            for (int i = 1; i <= numberOfScenes; i++)
            {
                string id = i.ToString();
                Texture2D backgroundTexture = Content.Load<Texture2D>(id+"/background");
                backgrounds.Add(backgroundTexture);
            }
            
            // Load sounds
            HitSound = Content.Load<SoundEffect>("collect");
            SpecialGoodSound = Content.Load<SoundEffect>("woof");
            MissSound = Content.Load<SoundEffect>("miss");
            SpecialBadSound = Content.Load<SoundEffect>("hiss");
            GameOverfSound = Content.Load<SoundEffect>("gameover");
            Music = Content.Load<Song>("music");
            MediaPlayer.IsRepeating = true;
            MediaPlayer.Volume = 0.4f;

            // Load font
            font = Content.Load<SpriteFont>("score");

            // Start playing background music
            MediaPlayer.Play(Music);

            Background = new Background((Texture2D)backgrounds[backgroundId], screenBounds.Width, screenBounds.Height);
            SpritesForDrawList.Add(Background);
            SpritesForDrawList.Add(Bag);

            LivesPosition = new Vector2(screenBounds.Right - 120, 10);

            // Create new gift object and set its initial position
            Gift first = new Gift((Texture2D)giftTextures[0 + backgroundId * 4], false, HitSound, 10, 0);
            float height = screenBounds.Top - first.Size.Height;
            first.Position = new Vector2((screenBounds.Right - Bag.Size.Width) / 2, height);
            Gifts.Add(first);
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// game-specific content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {

            // Play game mode
            if (CurrentState == States.Play)
            {

                var bounds = graphics.GraphicsDevice.Viewport.Bounds;

                // Registering user's moves.
                moveBag(bounds, Bag.Speed * gameTime.ElapsedGameTime.TotalMilliseconds);

                // Move gifts
                moveGifts(bounds, gameTime);

                // Generate new gift when the las one is far enough.
                generateMoreGifts();

                // Game over
                if (Lives == 0)
                {
                    CurrentState = States.Result;
                    GameOverfSound.Play();
                }

            }

            // Show rsults mode
            else
            {
                var touchState = Keyboard.GetState();
                if (touchState.IsKeyDown(Keys.Space))
                {
                    reset();
                    CurrentState = States.Play;
                }
            }

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);

            Color colorPlay = Color.White;
            Color colorResult = Color.White;
            if (CurrentState == States.Result)
            {
                colorPlay = Color.White * 0.5f;
            }

            spriteBatch.Begin();
            for (int i = 0; i < SpritesForDrawList.Count; i++)
            {
                ///SpritesForDrawList.GetElement(i).Draw(spriteBatch);
                ((Sprite) SpritesForDrawList[i]).Draw(spriteBatch, colorPlay);
            }

            for (int i = 0; i < Gifts.Count; i++)
            {
                ///SpritesForDrawList.GetElement(i).Draw(spriteBatch);
                ((Sprite)Gifts[i]).Draw(spriteBatch, colorPlay);
            }

            System.String ScoreString = System.String.Concat("Score: ", Score);
            spriteBatch.DrawString(font, ScoreString, ScorePosition, Color.Black);

            System.String LivesString = System.String.Concat("Lives: ", Lives);
            spriteBatch.DrawString(font, LivesString, LivesPosition, Color.Black);

            if (CurrentState == States.Result)
            {
                sr.Draw(spriteBatch, colorResult);
                spriteBatch.DrawString(font, "Press SPACE to try again", PressSpacePosition, Color.Black);
            }

            // End drawing.
            // Send all gathered details to the graphic card in one batch.
            spriteBatch.End();
            base.Draw(gameTime);
        }

        /// <summary>
        /// Resets the game to the starting point.
        /// </summary>
        public void reset()
        {
            specialCounter = random.Next(15, 20);
            Gifts = new ArrayList();
            ZigZag = false;
            Score = 0;
            Lives = 3;
            Gift.Speed = Gift.InitialSpeed;
            
            var screenBounds = GraphicsDevice.Viewport.Bounds;

            backgroundId = (backgroundId + 1) % numberOfScenes;
            Background = new Background((Texture2D)backgrounds[backgroundId], screenBounds.Width, screenBounds.Height);
            SpritesForDrawList[0] = Background;

            Gifts = new ArrayList();
            Gift firstNew = new Gift((Texture2D)giftTextures[0 + backgroundId * 4], false, HitSound, 10, 0);
            float heightNew = screenBounds.Top - firstNew.Size.Height;
            firstNew.Position = new Vector2((screenBounds.Right - Bag.Size.Width) / 2, heightNew);
            Gifts.Add(firstNew);

        }

        private void generateMoreGifts()
        {
            var bounds = graphics.GraphicsDevice.Viewport.Bounds;
            int NumberOfGifts = Gifts.Count;
            if (((Gift)Gifts[NumberOfGifts - 1]).Position.Y > spaceBeetweenGifts)
            {
                int GiftImageIndex = random.Next(0, 4);
                Gift g = new Gift((Texture2D)giftTextures[GiftImageIndex + backgroundId * 4], ZigZag, HitSound, 10, 0);
                float y = bounds.Top - g.Size.Height;
                int padding = g.Size.Width + 20;
                float x = random.Next(padding, bounds.Width - padding);
                g.Position = new Vector2(x, y);
                Gifts.Add(g);
            }
        }

        private void moveGifts(Rectangle bounds, GameTime gameTime)
        {
            // Move gifts
            for (int i = 0; i < Gifts.Count; i++)
            {
                Gift g = ((Gift)Gifts[i]);
                g.Position += g.Direction * (float)(gameTime.ElapsedGameTime.TotalMilliseconds * Gift.Speed);

                // bounce of the Walls
                if (g.Position.X < bounds.Left || g.Position.X > bounds.Right - g.Size.Width)
                {
                    g.Direction.X = -g.Direction.X;
                }

                // Bag - gift collision
                if (CollisionDetector.Overlaps(g, Bag))
                {
                    Score += g.pointsCollected;
                    Lives += g.livesAffected;
                    if (ZigZag)
                    {
                        Score += g.pointsCollected;
                    }
                    Gift.accelerate();
                    Gifts.RemoveAt(i);
                    i--;
                    specialCounter--;
                    if (Score >= 100)
                    {
                        ZigZag = true;
                    }
                    g.hitSound.Play();
                }

                // if special gift is activated
                if (specialGift)
                {

                    // 1UP
                    if (specialUp)
                    {
                        if (CollisionDetector.Overlaps(Special, Bag))
                        {
                            specialGift = false;
                        }
                    }

                    // Do not lose life if user misses special gift.
                    if (Special.Position.Y > bounds.Bottom)
                    {
                        specialGift = false;
                    }

                }

                // create special gift
                if (specialCounter == 0)
                {
                    specialGift = true;
                    specialUp = !specialUp;
                    specialCounter = random.Next(10, 15);

                    Special = new Gift((Texture2D)specialTextures[0], true, SpecialGoodSound, 10, 1);

                    if (!specialUp)
                    {
                        Special = new Gift((Texture2D)specialTextures[1], true, SpecialBadSound, 125, -1);
                    }

                    float y = bounds.Top - Special.Size.Height;
                    int padding = Special.Size.Width + 20;
                    float x = random.Next(padding, bounds.Width - padding);
                    Special.Position = new Vector2(x, y);
                    Gifts.Add(Special);
                }

                // miss gift
                if (g.Position.Y > bounds.Bottom)
                {
                    Lives--;
                    Gifts.RemoveAt(i);
                    i--;
                    MissSound.Play();
                }
            }
        }

        private void moveBag(Rectangle bounds, double delta)
        {
            var touchState = Keyboard.GetState();
            if (touchState.IsKeyDown(Keys.Left))
            {
                Bag.Position.X -= (float)(delta);
            }

            if (touchState.IsKeyDown(Keys.Right))
            {
                Bag.Position.X += (float)(delta);
            }

            // Do not allow user to move outside the window
            Bag.Position.X = MathHelper.Clamp(Bag.Position.X,
                bounds.Left,
                bounds.Right - Bag.Size.Width);
        }
    }
}
